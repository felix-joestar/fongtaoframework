package com.fongtaoframework.starter.admin.modules.rights.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResIdParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysResFacade;
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
@RequestMapping("/sys-res")
public class SysResController {
    private final ISysResFacade sysResFacade;

    @PostMapping(RequestPathConstants.PAGE) @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RES_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysResPageParam param) { return R.success(sysResFacade.page(param)); }
    @PostMapping(RequestPathConstants.TREE) @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RES_TREE + "')")
    public R<List<SysResRow>> tree() { return R.success(sysResFacade.tree()); }
    @PostMapping(RequestPathConstants.GET_BY_ID) @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RES_GET_BY_ID + "')")
    public R<SysResRow> getById(@Valid @RequestBody SysResIdParam param) { return R.success(sysResFacade.getById(param.sysResId())); }
    @PostMapping(RequestPathConstants.CREATE) @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RES_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysResCreateParam param) { sysResFacade.create(param); return R.success(); }
    @PostMapping(RequestPathConstants.UPDATE_BY_ID) @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RES_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysResUpdateParam param) { sysResFacade.updateById(param); return R.success(); }
    @PostMapping(RequestPathConstants.DELETE_BY_ID) @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_RES_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysResIdParam param) { sysResFacade.deleteById(param.sysResId()); return R.success(); }
}
