package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import java.util.List;

public interface ISysRoleAuthService {

    List<SysRes> listAuthResources(String sysRoleId);

    void replaceAuthResources(String sysRoleId, List<SysRoleAuth> sysRoleAuths);
}
