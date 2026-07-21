package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import java.util.List;

public interface ISysDictService {

    PageResult<SysDict> page(PageQuery pageQuery);

    SysDict get(String sysDictId);

    void create(SysDict entity);

    void updateById(SysDict entity);

    void deleteById(String sysDictId);

    List<SysDictItem> listEnabledItems(String sysDictCode);
}
