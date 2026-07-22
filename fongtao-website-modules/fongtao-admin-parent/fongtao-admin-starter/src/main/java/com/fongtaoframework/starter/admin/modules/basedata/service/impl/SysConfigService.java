package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysConfigMapper;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysConfigService implements ISysConfigService {

    private final SysConfigMapper sysConfigMapper;

    @Override
    public PageResult<SysConfig> page(PageQuery pageQuery) {
        Page<SysConfig> page = sysConfigMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysConfig>().orderByDesc(SysConfig::getCreateTime));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysConfig findById(String sysConfigId) {
        return sysConfigMapper.selectById(sysConfigId);
    }

    @Override
    public SysConfig findByCode(String sysConfigCode) {
        return sysConfigMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getSysConfigCode, sysConfigCode).last("limit 1"));
    }

    @Override
    public boolean existsByCode(String sysConfigCode, String excludedSysConfigId) {
        return sysConfigMapper.selectCount(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getSysConfigCode, sysConfigCode)
                .ne(excludedSysConfigId != null, SysConfig::getSysConfigId, excludedSysConfigId)) > 0;
    }

    @Override
    public boolean save(SysConfig entity) {
        return sysConfigMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysConfig entity) {
        return sysConfigMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysConfigId) {
        return sysConfigMapper.deleteById(sysConfigId) == 1;
    }
}
