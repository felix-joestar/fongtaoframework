package com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysSerialUpdateParam(
        @NotBlank String sysSerialId,
        @NotBlank @Size(max = 64) String serialCode,
        @NotBlank @Size(max = 128) String serialName,
        @Size(max = 64) String serialPrefix,
        @Size(max = 32) String datePattern,
        @NotNull Integer serialLength,
        @NotNull Long currentValue,
        @NotNull Integer stepValue,
        @NotBlank @Size(max = 32) String cycleType,
        @NotNull Integer enabled,
        @Size(max = 500) String remark
) {
}
