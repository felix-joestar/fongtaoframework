package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysSerialMapper;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysSerialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysSerialService implements ISysSerialService {

    private final SysSerialMapper sysSerialMapper;

    @Override
    public PageResult<SysSerial> page(PageQuery pageQuery) {
        Page<SysSerial> page = sysSerialMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysSerial>().orderByAsc(SysSerial::getSerialCode));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysSerial findById(String sysSerialId) {
        return sysSerialMapper.selectById(sysSerialId);
    }

    @Override
    public SysSerial findByCodeForUpdate(String serialCode) {
        return sysSerialMapper.selectByCodeForUpdate(serialCode);
    }

    @Override
    public boolean existsByCode(String serialCode, String excludedSysSerialId) {
        return sysSerialMapper.selectCount(new LambdaQueryWrapper<SysSerial>()
                .eq(SysSerial::getSerialCode, serialCode)
                .ne(excludedSysSerialId != null, SysSerial::getSysSerialId, excludedSysSerialId)) > 0;
    }

    @Override
    public boolean save(SysSerial entity) {
        return sysSerialMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysSerial entity) {
        return sysSerialMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysSerialId) {
        return sysSerialMapper.deleteById(sysSerialId) == 1;
    }
}
