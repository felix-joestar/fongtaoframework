package com.fongtaoframework.starter.admin.modules.rights.domain.dto;

import java.util.List;

public record SysResRow(
        String sysResId, String parentId, String sysResCode, String sysResName, String sysResType,
        String permissionCode, String routePath, String componentPath, String icon, Integer visibled,
        Integer enabled, Integer sortNo, String sysData, String remark, List<SysResRow> children
) {

    public static SysResRow withChildren(SysResRow row, List<SysResRow> children) {
        return new SysResRow(row.sysResId, row.parentId, row.sysResCode, row.sysResName, row.sysResType,
                row.permissionCode, row.routePath, row.componentPath, row.icon, row.visibled, row.enabled,
                row.sortNo, row.sysData, row.remark, List.copyOf(children));
    }
}
