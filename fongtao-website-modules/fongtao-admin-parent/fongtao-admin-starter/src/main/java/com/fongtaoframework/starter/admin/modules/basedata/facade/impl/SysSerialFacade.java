package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import cn.hutool.core.util.StrUtil;
import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysSerialConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysSerialRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysSerialFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysSerialService;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysSerialFacade implements ISysSerialFacade {

    private static final Set<String> CYCLE_TYPES = Set.of("none", "day", "month", "year");

    private final ISysSerialService sysSerialService;
    private final SysSerialConverter sysSerialConverter;

    @Override
    public PageResult<SysSerialRow> page(SysSerialPageParam param) {
        SysSerialPageParam pageParam = param == null ? new SysSerialPageParam(null, null) : param;
        PageResult<SysSerial> page = sysSerialService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysSerialConverter::toRow);
    }

    @Override
    public SysSerialRow getById(String sysSerialId) {
        return sysSerialConverter.toRow(require(sysSerialId));
    }

    @Override
    @Transactional
    public void create(SysSerialCreateParam param) {
        SysSerial entity = sysSerialConverter.toEntity(param);
        assertValid(entity);
        assertCodeUnique(entity.getSerialCode(), null);
        if (!sysSerialService.save(entity)) {
            throw new BusinessException("流水号配置新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysSerialUpdateParam param) {
        SysSerial entity = sysSerialConverter.toEntity(param);
        require(entity.getSysSerialId());
        assertValid(entity);
        assertCodeUnique(entity.getSerialCode(), entity.getSysSerialId());
        if (!sysSerialService.updateById(entity)) {
            throw new BusinessException("流水号配置更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysSerialId) {
        require(sysSerialId);
        if (!sysSerialService.deleteById(sysSerialId)) {
            throw new BusinessException("流水号配置删除失败");
        }
    }

    @Override
    @Transactional
    public String next(String serialCode) {
        SysSerial serial = sysSerialService.findByCodeForUpdate(serialCode);
        if (serial == null || !Integer.valueOf(1).equals(serial.getEnabled())) {
            throw new BusinessException("流水号配置不存在或未启用");
        }
        String cycle = resolveCycle(serial.getCycleType());
        long current = cycle.equals(serial.getLastCycle()) ? serial.getCurrentValue() : 0L;
        long next = current + serial.getStepValue();
        serial.setCurrentValue(next);
        serial.setLastCycle(cycle);
        if (!sysSerialService.updateById(serial)) {
            throw new BusinessException("流水号递增失败");
        }
        return render(serial, next);
    }

    private SysSerial require(String sysSerialId) {
        SysSerial entity = sysSerialService.findById(sysSerialId);
        if (entity == null) {
            throw new BusinessException("流水号配置不存在或已删除");
        }
        return entity;
    }

    private void assertCodeUnique(String code, String currentId) {
        if (sysSerialService.existsByCode(code, currentId)) {
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
