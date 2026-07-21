package com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysDictUpdateParam(
        @NotBlank String sysDictId,
        @NotBlank @Size(max = 64) String sysDictCode,
        @NotBlank @Size(max = 128) String sysDictName,
        @NotNull Integer enabled,
        @Size(max = 500) String remark
) {
}
