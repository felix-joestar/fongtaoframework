package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysRoleUpdateParam(
        @NotBlank String sysRoleId,
        String parentId,
        @NotBlank @Size(max = 64) String sysRoleCode,
        @NotBlank @Size(max = 128) String sysRoleName,
        @NotNull Integer enabled,
        Integer sortNo,
        @Size(max = 500) String remark,
        @NotNull Long version
) {
}
