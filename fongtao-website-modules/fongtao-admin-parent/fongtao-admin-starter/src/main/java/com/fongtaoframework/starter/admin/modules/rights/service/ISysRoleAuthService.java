package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import java.util.List;

public interface ISysRoleAuthService {

    List<String> listResourceIdsByRoleId(String sysRoleId);

    List<SysRes> listEffectiveResourcesByUserId(String sysUserId);

    List<String> listEffectivePermissionCodesByUserId(String sysUserId);

    boolean existsByRoleId(String sysRoleId);

    boolean existsByResId(String sysResId);

    boolean replaceByRoleId(String sysRoleId, List<SysRoleAuth> sysRoleAuths);
}
