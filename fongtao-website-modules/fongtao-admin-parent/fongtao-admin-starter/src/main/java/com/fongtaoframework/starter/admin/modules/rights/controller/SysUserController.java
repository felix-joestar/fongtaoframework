package com.fongtaoframework.starter.admin.modules.rights.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserIdParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysUserRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdatePasswordParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateStatusParam;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysUserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-user")
public class SysUserController {

    private final ISysUserFacade sysUserFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_USER_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysUserPageParam param) {
        return R.success(sysUserFacade.page(param));
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_USER_GET_BY_ID + "')")
    public R<SysUserRow> getById(@Valid @RequestBody SysUserIdParam param) {
        return R.success(sysUserFacade.getById(param.sysUserId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_USER_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysUserCreateParam param) {
        sysUserFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_USER_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysUserUpdateParam param) {
        sysUserFacade.updateById(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_USER_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysUserIdParam param) {
        sysUserFacade.deleteById(param.sysUserId());
        return R.success();
    }

    @PostMapping("/update-status")
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_USER_UPDATE_STATUS + "')")
    public R<Void> updateStatus(@Valid @RequestBody SysUserUpdateStatusParam param) {
        sysUserFacade.updateStatus(param);
        return R.success();
    }

    @PostMapping("/update-password")
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_USER_UPDATE_PASSWORD + "')")
    public R<Void> updatePassword(@Valid @RequestBody SysUserUpdatePasswordParam param) {
        sysUserFacade.updatePassword(param);
        return R.success();
    }
}
