package com.fongtaoframework.starter.admin.modules.rights.domain.dto;

import java.util.List;

public record SysRightsRow(
        String sysRightsId, String sysUserId, String sysOrgId, String sysRoleId, Integer defaulted,
        Integer enabled, String dataScope, String remark, List<String> customSysOrgIds, Long version
) {
}
