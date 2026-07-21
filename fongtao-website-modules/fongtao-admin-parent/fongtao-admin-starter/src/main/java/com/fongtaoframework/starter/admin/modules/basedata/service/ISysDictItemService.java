package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;

public interface ISysDictItemService {

    PageResult<SysDictItem> page(PageQuery pageQuery);

    SysDictItem get(String sysDictItemId);

    void create(SysDictItem entity);

    void updateById(SysDictItem entity);

    void deleteById(String sysDictItemId);
}
