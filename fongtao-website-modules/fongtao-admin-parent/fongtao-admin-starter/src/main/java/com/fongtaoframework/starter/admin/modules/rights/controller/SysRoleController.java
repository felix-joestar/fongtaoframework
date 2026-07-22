package com.fongtaoframework.starter.admin.modules.rights.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleIdParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRoleRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRolePageParam;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRoleFacade;
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
@RequestMapping("/sys-role")
public class SysRoleController {

    private final ISysRoleFacade sysRoleFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ROLE_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysRolePageParam param) {
        return R.success(sysRoleFacade.page(param));
    }

    @PostMapping(RequestPathConstants.TREE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ROLE_TREE + "')")
    public R<List<SysRoleRow>> tree() {
        return R.success(sysRoleFacade.tree());
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ROLE_GET_BY_ID + "')")
    public R<SysRoleRow> getById(@Valid @RequestBody SysRoleIdParam param) {
        return R.success(sysRoleFacade.getById(param.sysRoleId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ROLE_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysRoleCreateParam param) {
        sysRoleFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ROLE_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysRoleUpdateParam param) {
        sysRoleFacade.updateById(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_ROLE_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysRoleIdParam param) {
        sysRoleFacade.deleteById(param.sysRoleId());
        return R.success();
    }
}
