package com.fongtaoframework.starter.admin.modules.rights.domain.dto;

import java.util.List;

public record SysOrgRow(
        String sysOrgId, String parentId, String sysOrgCode, String sysOrgName, String sysOrgType,
        Integer enabled, Integer sortNo, String remark, Long version, List<SysOrgRow> children
) {

    public static SysOrgRow withChildren(SysOrgRow row, List<SysOrgRow> children) {
        return new SysOrgRow(row.sysOrgId, row.parentId, row.sysOrgCode, row.sysOrgName, row.sysOrgType,
                row.enabled, row.sortNo, row.remark, row.version, List.copyOf(children));
    }
}
