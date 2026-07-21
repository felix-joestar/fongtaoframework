package com.fongtaoframework.starter.admin.modules.rights.domain.dto;

import java.util.List;

public record SysRoleRow(
        String sysRoleId, String parentId, String sysRoleCode, String sysRoleName,
        Integer enabled, Integer sortNo, String remark, List<SysRoleRow> children
) {
}
