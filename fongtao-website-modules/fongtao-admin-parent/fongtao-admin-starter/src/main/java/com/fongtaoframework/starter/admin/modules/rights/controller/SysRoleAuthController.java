package com.fongtaoframework.starter.admin.modules.rights.controller;

import com.fongtaoframework.core.R;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleAuthListParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleAuthResParam;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRoleAuthFacade;
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
@RequestMapping("/sys-role-auth")
public class SysRoleAuthController {
    private final ISysRoleAuthFacade sysRoleAuthFacade;
    @PostMapping("/list-auth-res") @PreAuthorize("hasAuthority('admin:sys-role-auth:list-auth-res')")
    public R<List<SysResRow>> listAuthResources(@Valid @RequestBody SysRoleAuthListParam param) {
        return R.success(sysRoleAuthFacade.listAuthResources(param.sysRoleId()));
    }
    @PostMapping("/auth-res") @PreAuthorize("hasAuthority('admin:sys-role-auth:auth-res')")
    public R<Void> replaceAuthResources(@Valid @RequestBody SysRoleAuthResParam param) {
        sysRoleAuthFacade.replaceAuthResources(param.sysRoleId(), param.sysResIds());
        return R.success();
    }
}
