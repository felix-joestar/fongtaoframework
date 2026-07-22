package com.fongtaoframework.starter.admin.modules.basedata.controller;

import com.fongtaoframework.starter.admin.common.constant.AdminPermissionCodes;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.admin.common.constant.RequestPathConstants;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemIdParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysDictItemFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-dict-item")
public class SysDictItemController {

    private final ISysDictItemFacade sysDictItemFacade;

    @PostMapping(RequestPathConstants.PAGE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_ITEM_PAGE + "')")
    public R<?> page(@RequestBody(required = false) SysDictItemPageParam param) {
        return R.success(sysDictItemFacade.page(param));
    }

    @PostMapping(RequestPathConstants.GET_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_ITEM_GET_BY_ID + "')")
    public R<SysDictItemRow> getById(@Valid @RequestBody SysDictItemIdParam param) {
        return R.success(sysDictItemFacade.getById(param.sysDictItemId()));
    }

    @PostMapping(RequestPathConstants.CREATE)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_ITEM_CREATE + "')")
    public R<Void> create(@Valid @RequestBody SysDictItemCreateParam param) {
        sysDictItemFacade.create(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.UPDATE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_ITEM_UPDATE_BY_ID + "')")
    public R<Void> updateById(@Valid @RequestBody SysDictItemUpdateParam param) {
        sysDictItemFacade.updateById(param);
        return R.success();
    }

    @PostMapping(RequestPathConstants.DELETE_BY_ID)
    @PreAuthorize("hasAuthority('" + AdminPermissionCodes.SYS_DICT_ITEM_DELETE_BY_ID + "')")
    public R<Void> deleteById(@Valid @RequestBody SysDictItemIdParam param) {
        sysDictItemFacade.deleteById(param.sysDictItemId());
        return R.success();
    }
}
