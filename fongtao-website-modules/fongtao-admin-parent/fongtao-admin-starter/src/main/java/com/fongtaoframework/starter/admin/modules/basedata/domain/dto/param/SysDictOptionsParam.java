package com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SysDictOptionsParam(@NotBlank @Size(max = 64) String sysDictCode) {
}
