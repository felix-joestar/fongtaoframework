package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;

public interface ISysDictService {

    PageResult<SysDict> page(PageQuery pageQuery);

    SysDict findById(String sysDictId);

    SysDict findEnabledByCode(String sysDictCode);

    boolean existsByCode(String sysDictCode, String excludedSysDictId);

    boolean save(SysDict entity);

    boolean updateById(SysDict entity);

    boolean deleteById(String sysDictId);
}
