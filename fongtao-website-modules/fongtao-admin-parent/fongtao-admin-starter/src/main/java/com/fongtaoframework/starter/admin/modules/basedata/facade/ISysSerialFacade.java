package com.fongtaoframework.starter.admin.modules.basedata.facade;

import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysSerialRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialUpdateParam;

public interface ISysSerialFacade {

    PageResult<SysSerialRow> page(SysSerialPageParam param);

    SysSerialRow getById(String sysSerialId);

    void create(SysSerialCreateParam param);

    void updateById(SysSerialUpdateParam param);

    void deleteById(String sysSerialId);

    String next(String serialCode);
}
