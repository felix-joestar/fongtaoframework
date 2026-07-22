package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;

public interface ISysConfigService {

    PageResult<SysConfig> page(PageQuery pageQuery);

    SysConfig findById(String sysConfigId);

    SysConfig findByCode(String sysConfigCode);

    boolean existsByCode(String sysConfigCode, String excludedSysConfigId);

    boolean save(SysConfig entity);

    boolean updateById(SysConfig entity);

    boolean deleteById(String sysConfigId);
}
