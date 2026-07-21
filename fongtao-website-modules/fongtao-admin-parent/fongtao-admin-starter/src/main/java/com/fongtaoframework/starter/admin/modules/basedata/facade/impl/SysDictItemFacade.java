package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import org.springframework.stereotype.Component;
import cn.hutool.core.util.IdUtil;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysDictItemConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysDictItemFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictItemService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysDictItemFacade implements ISysDictItemFacade {

    private final ISysDictItemService sysDictItemService;
    private final SysDictItemConverter sysDictItemConverter;

    @Override
    public PageResult<SysDictItemRow> page(SysDictItemPageParam param) {
        SysDictItemPageParam pageParam = param == null ? new SysDictItemPageParam(null, null) : param;
        PageResult<SysDictItem> page = sysDictItemService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysDictItemConverter::toRow).toList(), page.total(),
                page.pageNo(), page.pageSize());
    }

    @Override
    public SysDictItemRow getById(String sysDictItemId) {
        return sysDictItemConverter.toRow(sysDictItemService.get(sysDictItemId));
    }

    @Override
    public void create(SysDictItemCreateParam param) {
        SysDictItem entity = sysDictItemConverter.toEntity(param);
        entity.setSysDictItemId(IdUtil.simpleUUID());
        sysDictItemService.create(entity);
    }

    @Override
    public void updateById(SysDictItemUpdateParam param) {
        sysDictItemService.updateById(sysDictItemConverter.toEntity(param));
    }

    @Override
    public void deleteById(String sysDictItemId) {
        sysDictItemService.deleteById(sysDictItemId);
    }
}
