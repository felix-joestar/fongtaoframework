package com.fongtaoframework.starter.admin.modules.basedata.facade;

import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysConfigRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigUpdateParam;

public interface ISysConfigFacade {

    PageResult<SysConfigRow> page(SysConfigPageParam param);

    SysConfigRow getById(String sysConfigId);

    void create(SysConfigCreateParam param);

    void updateById(SysConfigUpdateParam param);

    void deleteById(String sysConfigId);
}
