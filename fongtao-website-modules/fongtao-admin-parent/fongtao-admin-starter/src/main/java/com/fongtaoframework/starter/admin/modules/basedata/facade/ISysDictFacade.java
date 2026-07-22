package com.fongtaoframework.starter.admin.modules.basedata.facade;

import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictUpdateParam;
import java.util.List;

public interface ISysDictFacade {

    PageResult<SysDictRow> page(SysDictPageParam param);

    SysDictRow getById(String sysDictId);

    void create(SysDictCreateParam param);

    void updateById(SysDictUpdateParam param);

    void deleteById(String sysDictId);

    List<SysDictItemRow> options(String sysDictCode);
}
