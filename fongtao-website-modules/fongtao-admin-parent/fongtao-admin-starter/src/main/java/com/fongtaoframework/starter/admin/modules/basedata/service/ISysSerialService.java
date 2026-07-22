package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;

public interface ISysSerialService {

    PageResult<SysSerial> page(PageQuery pageQuery);

    SysSerial findById(String sysSerialId);

    SysSerial findByCodeForUpdate(String serialCode);

    boolean existsByCode(String serialCode, String excludedSysSerialId);

    boolean save(SysSerial entity);

    boolean updateById(SysSerial entity);

    boolean deleteById(String sysSerialId);
}
