package com.fongtaoframework.starter.admin.modules.rights.domain.dto;

import java.util.List;

public record SysRoleRow(
        String sysRoleId, String parentId, String sysRoleCode, String sysRoleName,
        Integer enabled, Integer sortNo, String remark, Long version, List<SysRoleRow> children
) {

    public static SysRoleRow withChildren(SysRoleRow row, List<SysRoleRow> children) {
        return new SysRoleRow(row.sysRoleId, row.parentId, row.sysRoleCode, row.sysRoleName,
                row.enabled, row.sortNo, row.remark, row.version, List.copyOf(children));
    }
}
