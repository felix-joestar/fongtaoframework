package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import java.util.List;

public interface ISysDictItemService {

    PageResult<SysDictItem> page(PageQuery pageQuery);

    SysDictItem findById(String sysDictItemId);

    boolean existsByDictId(String sysDictId);

    boolean existsByDictIdAndValue(String sysDictId, String dictItemValue, String excludedSysDictItemId);

    List<SysDictItem> listEnabledByDictId(String sysDictId);

    boolean save(SysDictItem entity);

    boolean updateById(SysDictItem entity);

    boolean deleteById(String sysDictItemId);
}
