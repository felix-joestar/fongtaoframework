package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import org.springframework.stereotype.Component;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysRightsConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRightsRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateEnabledParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRightsFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysRightsFacade implements ISysRightsFacade {

    private final ISysRightsService sysRightsService;
    private final SysRightsConverter sysRightsConverter;

    @Override
    public PageResult<SysRightsRow> page(SysRightsPageParam param) {
        SysRightsPageParam pageParam = param == null ? new SysRightsPageParam(null, null) : param;
        PageResult<SysRights> page = sysRightsService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(this::toRow).toList(), page.total(), page.pageNo(), page.pageSize());
    }

    @Override
    public SysRightsRow getById(String sysRightsId) {
        return toRow(sysRightsService.get(sysRightsId));
    }

    @Override
    public void create(SysRightsCreateParam param) {
        sysRightsService.create(sysRightsConverter.toEntity(param), param.customSysOrgIds());
    }

    @Override
    public void updateById(SysRightsUpdateParam param) {
        sysRightsService.updateById(sysRightsConverter.toEntity(param), param.customSysOrgIds());
    }

    @Override
    public void updateEnabled(SysRightsUpdateEnabledParam param) {
        sysRightsService.updateEnabled(param.sysRightsId(), param.enabled());
    }

    @Override
    public void deleteById(String sysRightsId) {
        sysRightsService.deleteById(sysRightsId);
    }

    private SysRightsRow toRow(SysRights entity) {
        SysRightsRow row = sysRightsConverter.toRow(entity);
        List<String> customSysOrgIds = sysRightsService.listCustomOrgIds(entity.getSysRightsId());
        return new SysRightsRow(row.sysRightsId(), row.sysUserId(), row.sysOrgId(), row.sysRoleId(), row.defaulted(),
                row.enabled(), row.dataScope(), row.remark(), customSysOrgIds);
    }
}
