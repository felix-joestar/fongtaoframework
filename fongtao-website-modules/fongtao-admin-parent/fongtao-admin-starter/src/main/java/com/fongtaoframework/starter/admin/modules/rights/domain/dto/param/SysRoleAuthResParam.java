package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record SysRoleAuthResParam(@NotBlank String sysRoleId, List<String> sysResIds) {
}
