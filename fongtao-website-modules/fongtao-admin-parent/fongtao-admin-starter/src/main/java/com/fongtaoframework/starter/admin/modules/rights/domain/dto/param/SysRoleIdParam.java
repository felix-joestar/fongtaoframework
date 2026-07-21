package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;

public record SysRoleIdParam(@NotBlank String sysRoleId) {}
