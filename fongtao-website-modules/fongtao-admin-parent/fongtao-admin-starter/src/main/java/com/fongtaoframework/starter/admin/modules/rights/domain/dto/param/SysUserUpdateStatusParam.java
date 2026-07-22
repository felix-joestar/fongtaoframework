package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SysUserUpdateStatusParam(
        @NotBlank String sysUserId,
        @NotNull Integer sysUserStatus,
        @NotNull Long version
 ) {
}
