package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysConfigConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysConfigRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysConfigFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysConfigFacade implements ISysConfigFacade {

    private final ISysConfigService sysConfigService;
    private final SysConfigConverter sysConfigConverter;

    @Override
    public PageResult<SysConfigRow> page(SysConfigPageParam param) {
        SysConfigPageParam pageParam = param == null ? new SysConfigPageParam(null, null) : param;
        PageResult<SysConfig> page = sysConfigService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysConfigConverter::toRow);
    }

    @Override
    public SysConfigRow getById(String sysConfigId) {
        return sysConfigConverter.toRow(require(sysConfigId));
    }

    @Override
    @Transactional
    public void create(SysConfigCreateParam param) {
        SysConfig entity = sysConfigConverter.toEntity(param);
        assertCodeUnique(entity.getSysConfigCode(), null);
        if (!sysConfigService.save(entity)) {
            throw new BusinessException("系统配置新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysConfigUpdateParam param) {
        SysConfig entity = sysConfigConverter.toEntity(param);
        require(entity.getSysConfigId());
        assertCodeUnique(entity.getSysConfigCode(), entity.getSysConfigId());
        if (!sysConfigService.updateById(entity)) {
            throw new BusinessException("系统配置更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysConfigId) {
        require(sysConfigId);
        if (!sysConfigService.deleteById(sysConfigId)) {
            throw new BusinessException("系统配置删除失败");
        }
    }

    private SysConfig require(String sysConfigId) {
        SysConfig entity = sysConfigService.findById(sysConfigId);
        if (entity == null) {
            throw new BusinessException("系统配置不存在或已删除");
        }
        return entity;
    }

    private void assertCodeUnique(String code, String currentId) {
        if (sysConfigService.existsByCode(code, currentId)) {
            throw new BusinessException("系统配置编码已存在");
        }
    }
}
