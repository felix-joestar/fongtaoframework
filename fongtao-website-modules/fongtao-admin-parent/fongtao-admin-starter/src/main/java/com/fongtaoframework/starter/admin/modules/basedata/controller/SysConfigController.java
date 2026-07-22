package com.fongtaoframework.starter.admin.modules.basedata.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysConfigRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigIdParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysConfigFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-config")
public class SysConfigController {

    private final ISysConfigFacade sysConfigFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_CONFIG_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysConfigPageParam param) {
        return R.success(sysConfigFacade.page(param));
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_CONFIG_GET_BY_ID + "')")
    public R<SysConfigRow> getById(@Valid @RequestBody SysConfigIdParam param) {
        return R.success(sysConfigFacade.getById(param.sysConfigId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_CONFIG_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysConfigCreateParam param) {
        sysConfigFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_CONFIG_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysConfigUpdateParam param) {
        sysConfigFacade.updateById(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_CONFIG_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysConfigIdParam param) {
        sysConfigFacade.deleteById(param.sysConfigId());
        return R.success();
    }
}
