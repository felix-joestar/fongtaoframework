package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import java.util.List;

public interface ISysRoleService {

    PageResult<SysRole> page(PageQuery pageQuery);

    List<SysRole> list();

    SysRole get(String sysRoleId);

    void create(SysRole entity);

    void updateById(SysRole entity);

    void deleteById(String sysRoleId);
}
