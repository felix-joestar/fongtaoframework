package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysRightsUpdateParam(
        @NotBlank String sysRightsId,
        @NotBlank String sysUserId,
        @NotBlank String sysOrgId,
        @NotBlank String sysRoleId,
        @NotNull Integer defaulted,
        @NotNull Integer enabled,
        @NotBlank @Size(max = 32) String dataScope,
        @Size(max = 500) String remark,
        java.util.List<String> customSysOrgIds
) {
}
