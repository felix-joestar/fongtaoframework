package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import org.springframework.stereotype.Component;
import cn.hutool.core.util.IdUtil;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysConfigConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysConfigRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysConfigFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysConfigService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysConfigFacade implements ISysConfigFacade {

    private final ISysConfigService sysConfigService;
    private final SysConfigConverter sysConfigConverter;

    @Override
    public PageResult<SysConfigRow> page(SysConfigPageParam param) {
        SysConfigPageParam pageParam = param == null ? new SysConfigPageParam(null, null) : param;
        PageResult<SysConfig> page = sysConfigService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysConfigConverter::toRow).toList(), page.total(),
                page.pageNo(), page.pageSize());
    }

    @Override
    public SysConfigRow getById(String sysConfigId) {
        return sysConfigConverter.toRow(sysConfigService.get(sysConfigId));
    }

    @Override
    public void create(SysConfigCreateParam param) {
        SysConfig entity = sysConfigConverter.toEntity(param);
        entity.setSysConfigId(IdUtil.simpleUUID());
        sysConfigService.create(entity);
    }

    @Override
    public void updateById(SysConfigUpdateParam param) {
        sysConfigService.updateById(sysConfigConverter.toEntity(param));
    }

    @Override
    public void deleteById(String sysConfigId) {
        sysConfigService.deleteById(sysConfigId);
    }
}
