package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;

public interface ISysSerialService {

    PageResult<SysSerial> page(PageQuery pageQuery);

    SysSerial get(String sysSerialId);

    void create(SysSerial entity);

    void updateById(SysSerial entity);

    void deleteById(String sysSerialId);

    String next(String serialCode);
}
