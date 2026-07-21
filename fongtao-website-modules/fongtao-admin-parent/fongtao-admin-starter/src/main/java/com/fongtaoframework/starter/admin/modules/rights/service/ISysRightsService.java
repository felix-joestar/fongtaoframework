package com.fongtaoframework.starter.admin.modules.rights.service;

import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import java.util.List;

public interface ISysRightsService {

    PageResult<SysRights> page(PageQuery pageQuery);

    SysRights get(String sysRightsId);

    SysRights findDefaultByUserId(String sysUserId);

    List<String> listCustomOrgIds(String sysRightsId);

    void create(SysRights entity, List<String> customSysOrgIds);

    void updateById(SysRights entity, List<String> customSysOrgIds);

    void updateEnabled(String sysRightsId, Integer enabled);

    void deleteById(String sysRightsId);
}
