package com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param;

import jakarta.validation.constraints.NotBlank;

public record SysDictIdParam(@NotBlank String sysDictId) {}
