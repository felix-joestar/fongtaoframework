package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysConfigMapper;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public SysConfig get(String sysConfigId) {
        SysConfig entity = sysConfigMapper.selectById(sysConfigId);
        if (entity == null) {
            throw new BusinessException("系统配置不存在或已删除");
        }
        return entity;
    }

    @Override
    public SysConfig getByCode(String sysConfigCode) {
        return sysConfigMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getSysConfigCode, sysConfigCode).last("limit 1"));
    }

    @Override
    @Transactional
    public void create(SysConfig entity) {
        assertCodeUnique(entity.getSysConfigCode(), null);
        if (sysConfigMapper.insert(entity) != 1) {
            throw new BusinessException("系统配置新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysConfig entity) {
        get(entity.getSysConfigId());
        assertCodeUnique(entity.getSysConfigCode(), entity.getSysConfigId());
        if (sysConfigMapper.updateById(entity) != 1) {
            throw new BusinessException("系统配置更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysConfigId) {
        get(sysConfigId);
        if (sysConfigMapper.deleteById(sysConfigId) != 1) {
            throw new BusinessException("系统配置删除失败");
        }
    }

    private void assertCodeUnique(String code, String currentId) {
        if (sysConfigMapper.selectCount(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getSysConfigCode, code)
                .ne(StrUtil.isNotBlank(currentId), SysConfig::getSysConfigId, currentId)) > 0) {
            throw new BusinessException("系统配置编码已存在");
        }
    }
}
