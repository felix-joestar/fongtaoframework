package com.fongtaoframework.starter.admin.modules.basedata.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictIdParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictOptionsParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysDictFacade;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-dict")
public class SysDictController {

    private final ISysDictFacade sysDictFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysDictPageParam param) {
        return R.success(sysDictFacade.page(param));
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_GET_BY_ID + "')")
    public R<SysDictRow> getById(@Valid @RequestBody SysDictIdParam param) {
        return R.success(sysDictFacade.getById(param.sysDictId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysDictCreateParam param) {
        sysDictFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysDictUpdateParam param) {
        sysDictFacade.updateById(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysDictIdParam param) {
        sysDictFacade.deleteById(param.sysDictId());
        return R.success();
    }

    @PostMapping("/options")
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_OPTIONS + "')")
    public R<List<SysDictItemRow>> options(@Valid @RequestBody SysDictOptionsParam param) {
        return R.success(sysDictFacade.options(param.sysDictCode()));
    }
}
