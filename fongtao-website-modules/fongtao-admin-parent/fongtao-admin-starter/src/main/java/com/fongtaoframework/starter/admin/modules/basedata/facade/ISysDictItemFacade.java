package com.fongtaoframework.starter.admin.modules.basedata.facade;

import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemUpdateParam;

public interface ISysDictItemFacade {

    PageResult<SysDictItemRow> page(SysDictItemPageParam param);

    SysDictItemRow getById(String sysDictItemId);

    void create(SysDictItemCreateParam param);

    void updateById(SysDictItemUpdateParam param);

    void deleteById(String sysDictItemId);
}
