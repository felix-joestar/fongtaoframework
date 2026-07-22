package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysOrgUpdateParam(
        @NotBlank String sysOrgId,
        String parentId,
        @NotBlank @Size(max = 64) String sysOrgCode,
        @NotBlank @Size(max = 128) String sysOrgName,
        @NotBlank @Size(max = 32) String sysOrgType,
        @NotNull Integer enabled,
        Integer sortNo,
        @Size(max = 500) String remark,
        @NotNull Long version
) {
}
