package com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysConfigUpdateParam(
        @NotBlank String sysConfigId,
        @NotBlank @Size(max = 64) String sysConfigCode,
        @NotBlank @Size(max = 128) String sysConfigName,
        String sysConfigValue,
        @NotBlank @Size(max = 32) String valueType,
        @NotNull Integer editable,
        @NotNull Integer enabled,
        @Size(max = 500) String remark
) {
}
