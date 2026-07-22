package com.fongtaoframework.starter.admin.modules.rights.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgIdParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysOrgRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysOrgFacade;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-org")
public class SysOrgController {

    private final ISysOrgFacade sysOrgFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ORG_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysOrgPageParam param) {
        return R.success(sysOrgFacade.page(param));
    }

    @PostMapping(RequestPathConstants.TREE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ORG_TREE + "')")
    public R<List<SysOrgRow>> tree() {
        return R.success(sysOrgFacade.tree());
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ORG_GET_BY_ID + "')")
    public R<SysOrgRow> getById(@Valid @RequestBody SysOrgIdParam param) {
        return R.success(sysOrgFacade.getById(param.sysOrgId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ORG_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysOrgCreateParam param) {
        sysOrgFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ORG_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysOrgUpdateParam param) {
        sysOrgFacade.updateById(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ORG_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysOrgIdParam param) {
        sysOrgFacade.deleteById(param.sysOrgId());
        return R.success();
    }
}
