package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysRightsUpdateEnabledParam(
        @NotBlank String sysRightsId,
        @NotNull Integer enabled
) {
}
