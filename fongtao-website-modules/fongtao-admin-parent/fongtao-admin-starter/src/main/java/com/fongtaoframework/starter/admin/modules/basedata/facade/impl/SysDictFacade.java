package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import org.springframework.stereotype.Component;
import cn.hutool.core.util.IdUtil;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysDictConverter;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysDictItemConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysDictFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysDictFacade implements ISysDictFacade {

    private final ISysDictService sysDictService;
    private final SysDictConverter sysDictConverter;
    private final SysDictItemConverter sysDictItemConverter;

    @Override
    public PageResult<SysDictRow> page(SysDictPageParam param) {
        SysDictPageParam pageParam = param == null ? new SysDictPageParam(null, null) : param;
        PageResult<SysDict> page = sysDictService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysDictConverter::toRow).toList(), page.total(),
                page.pageNo(), page.pageSize());
    }

    @Override
    public SysDictRow getById(String sysDictId) {
        return sysDictConverter.toRow(sysDictService.get(sysDictId));
    }

    @Override
    public void create(SysDictCreateParam param) {
        SysDict entity = sysDictConverter.toEntity(param);
        entity.setSysDictId(IdUtil.simpleUUID());
        sysDictService.create(entity);
    }

    @Override
    public void updateById(SysDictUpdateParam param) {
        sysDictService.updateById(sysDictConverter.toEntity(param));
    }

    @Override
    public void deleteById(String sysDictId) {
        sysDictService.deleteById(sysDictId);
    }

    @Override
    public List<SysDictItemRow> options(String sysDictCode) {
        return sysDictService.listEnabledItems(sysDictCode).stream().map(sysDictItemConverter::toRow).toList();
    }
}
