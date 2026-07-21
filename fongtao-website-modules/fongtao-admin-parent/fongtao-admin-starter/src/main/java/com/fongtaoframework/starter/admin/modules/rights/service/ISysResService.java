package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import java.util.List;

public interface ISysResService {

    PageResult<SysRes> page(PageQuery pageQuery);

    List<SysRes> list();

    List<SysRes> listByRoleId(String sysRoleId);

    List<String> listEnabledPermissionCodesByUserId(String sysUserId);

    List<SysRes> listVisibleByUserId(String sysUserId);

    SysRes get(String sysResId);

    void create(SysRes entity);

    void updateById(SysRes entity);

    void deleteById(String sysResId);
}
