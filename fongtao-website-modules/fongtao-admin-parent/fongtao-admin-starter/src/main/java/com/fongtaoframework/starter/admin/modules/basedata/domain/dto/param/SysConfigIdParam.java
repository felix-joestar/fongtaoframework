package com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param;

import jakarta.validation.constraints.NotBlank;

public record SysConfigIdParam(@NotBlank String sysConfigId) {}
