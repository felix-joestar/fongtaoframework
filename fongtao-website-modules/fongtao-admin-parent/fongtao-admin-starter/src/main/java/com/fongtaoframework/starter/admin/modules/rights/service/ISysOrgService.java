package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import java.util.List;

public interface ISysOrgService {

    PageResult<SysOrg> page(PageQuery pageQuery);

    List<SysOrg> list();

    SysOrg findById(String sysOrgId);

    boolean existsByCode(String sysOrgCode, String excludedSysOrgId);

    boolean existsByParentId(String parentId);

    boolean save(SysOrg entity);

    boolean updateById(SysOrg entity);

    boolean deleteById(String sysOrgId);
}
