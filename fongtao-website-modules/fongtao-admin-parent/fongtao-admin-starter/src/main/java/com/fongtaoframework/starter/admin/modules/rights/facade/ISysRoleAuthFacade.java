package com.fongtaoframework.starter.admin.modules.rights.facade;

import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import java.util.List;

public interface ISysRoleAuthFacade {

    List<SysResRow> listAuthResources(String sysRoleId);

    void replaceAuthResources(String sysRoleId, List<String> sysResIds);
}
