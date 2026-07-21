package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import java.util.List;

public interface ISysOrgService {

    PageResult<SysOrg> page(PageQuery pageQuery);

    List<SysOrg> list();

    SysOrg get(String sysOrgId);

    void create(SysOrg entity);

    void updateById(SysOrg entity);

    void deleteById(String sysOrgId);
}
