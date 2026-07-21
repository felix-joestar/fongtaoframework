package com.fongtaoframework.starter.admin.modules.basedata.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;

public interface ISysConfigService {

    PageResult<SysConfig> page(PageQuery pageQuery);

    SysConfig get(String sysConfigId);

    SysConfig getByCode(String sysConfigCode);

    void create(SysConfig entity);

    void updateById(SysConfig entity);

    void deleteById(String sysConfigId);
}
