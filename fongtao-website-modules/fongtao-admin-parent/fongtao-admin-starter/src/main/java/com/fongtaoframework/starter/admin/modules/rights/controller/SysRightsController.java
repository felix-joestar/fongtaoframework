package com.fongtaoframework.starter.admin.modules.rights.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRightsRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsIdParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateEnabledParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRightsFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-rights")
public class SysRightsController {

    private final ISysRightsFacade sysRightsFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RIGHTS_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysRightsPageParam param) {
        return R.success(sysRightsFacade.page(param));
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RIGHTS_GET_BY_ID + "')")
    public R<SysRightsRow> getById(@Valid @RequestBody SysRightsIdParam param) {
        return R.success(sysRightsFacade.getById(param.sysRightsId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RIGHTS_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysRightsCreateParam param) {
        sysRightsFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RIGHTS_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysRightsUpdateParam param) {
        sysRightsFacade.updateById(param);
        return R.success();
    }

    @PostMapping("/update-enabled")
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RIGHTS_UPDATE_ENABLED + "')")
    public R<Void> updateEnabled(@Valid @RequestBody SysRightsUpdateEnabledParam param) {
        sysRightsFacade.updateEnabled(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RIGHTS_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysRightsIdParam param) {
        sysRightsFacade.deleteById(param.sysRightsId());
        return R.success();
    }
}
