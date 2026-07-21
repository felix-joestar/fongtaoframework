package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysSerialMapper;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysSerialService;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysSerialService implements ISysSerialService {

    private static final Set<String> CYCLE_TYPES = Set.of("none", "day", "month", "year");

    private final SysSerialMapper sysSerialMapper;

    @Override
    public PageResult<SysSerial> page(PageQuery pageQuery) {
        Page<SysSerial> page = sysSerialMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysSerial>().orderByAsc(SysSerial::getSerialCode));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysSerial get(String sysSerialId) {
        SysSerial entity = sysSerialMapper.selectById(sysSerialId);
        if (entity == null) {
            throw new BusinessException("流水号配置不存在或已删除");
        }
        return entity;
    }

    @Override
    @Transactional
    public void create(SysSerial entity) {
        assertValid(entity);
        assertCodeUnique(entity.getSerialCode(), null);
        if (sysSerialMapper.insert(entity) != 1) {
            throw new BusinessException("流水号配置新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysSerial entity) {
        get(entity.getSysSerialId());
        assertValid(entity);
        assertCodeUnique(entity.getSerialCode(), entity.getSysSerialId());
        if (sysSerialMapper.updateById(entity) != 1) {
            throw new BusinessException("流水号配置更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysSerialId) {
        get(sysSerialId);
        if (sysSerialMapper.deleteById(sysSerialId) != 1) {
            throw new BusinessException("流水号配置删除失败");
        }
    }

    @Override
    @Transactional
    public String next(String serialCode) {
        SysSerial serial = sysSerialMapper.selectByCodeForUpdate(serialCode);
        if (serial == null || !Integer.valueOf(1).equals(serial.getEnabled())) {
            throw new BusinessException("流水号配置不存在或未启用");
        }
        String cycle = resolveCycle(serial.getCycleType());
        long current = cycle.equals(serial.getLastCycle()) ? serial.getCurrentValue() : 0L;
        long next = current + serial.getStepValue();
        serial.setCurrentValue(next);
        serial.setLastCycle(cycle);
        if (sysSerialMapper.updateById(serial) != 1) {
            throw new BusinessException("流水号递增失败");
        }
        return render(serial, next);
    }

    private void assertCodeUnique(String code, String currentId) {
        if (sysSerialMapper.selectCount(new LambdaQueryWrapper<SysSerial>().eq(SysSerial::getSerialCode, code)
                .ne(StrUtil.isNotBlank(currentId), SysSerial::getSysSerialId, currentId)) > 0) {
            throw new BusinessException("流水号编码已存在");
        }
    }

    private void assertValid(SysSerial serial) {
        if (serial.getSerialLength() == null || serial.getSerialLength() < 1
                || serial.getStepValue() == null || serial.getStepValue() < 1) {
            throw new BusinessException("流水号长度和步长必须大于零");
        }
        if (!CYCLE_TYPES.contains(serial.getCycleType())) {
            throw new BusinessException("流水号循环类型不支持");
        }
        if (StrUtil.isNotBlank(serial.getDatePattern())) {
            DateTimeFormatter.ofPattern(serial.getDatePattern());
        }
    }

    private String resolveCycle(String cycleType) {
        return switch (cycleType) {
            case "none" -> "none";
            case "day" -> LocalDate.now().toString();
            case "month" -> YearMonth.now().toString();
            case "year" -> Year.now().toString();
            default -> throw new BusinessException("流水号循环类型不支持");
        };
    }

    private String render(SysSerial serial, long number) {
        String date = StrUtil.isBlank(serial.getDatePattern()) ? ""
                : LocalDate.now().format(DateTimeFormatter.ofPattern(serial.getDatePattern()));
        return StrUtil.nullToEmpty(serial.getSerialPrefix()) + date
                + String.format("%0" + serial.getSerialLength() + "d", number);
    }
}
