package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import java.util.List;

public interface ISysRoleService {

    PageResult<SysRole> page(PageQuery pageQuery);

    List<SysRole> list();

    SysRole findById(String sysRoleId);

    boolean existsByCode(String sysRoleCode, String excludedSysRoleId);

    boolean existsByParentId(String parentId);

    boolean save(SysRole entity);

    boolean updateById(SysRole entity);

    boolean deleteById(String sysRoleId);
}
