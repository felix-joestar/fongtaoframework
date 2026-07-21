package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record SysUserUpdatePasswordParam(@NotBlank String sysUserId, @NotBlank @Size(min = 6, max = 128) String password) {
}
