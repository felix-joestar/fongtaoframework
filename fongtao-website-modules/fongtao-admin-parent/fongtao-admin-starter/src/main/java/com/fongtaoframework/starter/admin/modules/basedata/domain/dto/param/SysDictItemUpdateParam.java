package com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysDictItemUpdateParam(
        @NotBlank String sysDictItemId,
        @NotBlank String sysDictId,
        @NotBlank @Size(max = 128) String dictItemValue,
        @NotBlank @Size(max = 128) String dictItemLabel,
        @NotNull Integer enabled,
        Integer sortNo,
        @Size(max = 500) String remark,
        @NotNull Long version
) {
}
