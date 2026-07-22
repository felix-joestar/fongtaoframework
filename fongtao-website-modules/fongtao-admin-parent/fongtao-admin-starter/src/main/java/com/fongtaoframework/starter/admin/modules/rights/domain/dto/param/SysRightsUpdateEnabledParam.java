package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SysRightsUpdateEnabledParam(
        @NotBlank String sysRightsId,
        @NotNull Integer enabled,
        @NotNull Long version
 ) {
}
