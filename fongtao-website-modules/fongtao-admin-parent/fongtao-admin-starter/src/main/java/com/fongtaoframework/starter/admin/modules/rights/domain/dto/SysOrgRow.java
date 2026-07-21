package com.fongtaoframework.starter.admin.modules.rights.domain.dto;

import java.util.List;

public record SysOrgRow(
        String sysOrgId, String parentId, String sysOrgCode, String sysOrgName, String sysOrgType,
        Integer enabled, Integer sortNo, String remark, List<SysOrgRow> children
) {
}
