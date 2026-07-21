package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysResCreateParam(
        String parentId,
        @NotBlank @Size(max = 64) String sysResCode,
        @NotBlank @Size(max = 128) String sysResName,
        @NotBlank @Size(max = 32) String sysResType,
        @Size(max = 128) String permissionCode,
        @Size(max = 256) String routePath,
        @Size(max = 256) String componentPath,
        @Size(max = 128) String icon,
        @NotNull Integer visibled,
        @NotNull Integer enabled,
        Integer sortNo,
        String sysData,
        @Size(max = 500) String remark
) {
}
