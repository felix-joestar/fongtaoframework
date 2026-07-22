package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import java.util.List;

public interface ISysRightsService {

    PageResult<SysRights> page(PageQuery pageQuery);

    SysRights findById(String sysRightsId);

    SysRights findDefaultByUserId(String sysUserId);

    List<String> listCustomOrgIds(String sysRightsId);

    boolean existsBySysUserId(String sysUserId);

    boolean existsBySysOrgId(String sysOrgId);

    boolean existsExtraBySysOrgId(String sysOrgId);

    boolean existsBySysRoleId(String sysRoleId);

    boolean existsByUserOrgRole(String sysUserId, String sysOrgId, String sysRoleId, String excludedSysRightsId);

    void clearDefaulted(String sysUserId, String excludedSysRightsId);

    boolean save(SysRights entity);

    boolean updateById(SysRights entity);

    boolean deleteById(String sysRightsId);

    boolean replaceCustomSysOrgs(String sysRightsId, List<String> sysOrgIds);
}
