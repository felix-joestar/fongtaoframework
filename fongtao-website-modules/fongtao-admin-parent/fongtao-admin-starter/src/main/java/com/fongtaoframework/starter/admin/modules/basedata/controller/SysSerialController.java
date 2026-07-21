package com.fongtaoframework.starter.admin.modules.basedata.controller;

import com.fongtaoframework.core.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysSerialRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialIdParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialNextParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysSerialFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-serial")
public class SysSerialController {

    private final ISysSerialFacade sysSerialFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('admin:sys-serial:page')")
    public R<?> page(@RequestBody(required = false) SysSerialPageParam param) {
        return R.success(sysSerialFacade.page(param));
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('admin:sys-serial:get-by-id')")
    public R<SysSerialRow> getById(@Valid @RequestBody SysSerialIdParam param) {
        return R.success(sysSerialFacade.getById(param.sysSerialId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('admin:sys-serial:create')")
    public R<Void> create(@Valid @RequestBody SysSerialCreateParam param) {
        sysSerialFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('admin:sys-serial:update-by-id')")
    public R<Void> updateById(@Valid @RequestBody SysSerialUpdateParam param) {
        sysSerialFacade.updateById(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('admin:sys-serial:delete-by-id')")
    public R<Void> deleteById(@Valid @RequestBody SysSerialIdParam param) {
        sysSerialFacade.deleteById(param.sysSerialId());
        return R.success();
    }

    @PostMapping("/next")
    @PreAuthorize("hasAuthority('admin:sys-serial:next')")
    public R<String> next(@Valid @RequestBody SysSerialNextParam param) {
        return R.success(sysSerialFacade.next(param.serialCode()));
    }
}
