package com.fongtaoframework.starter.admin.modules.rights.domain.dto.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysUserUpdateParam(
        @NotBlank String sysUserId,
        @NotBlank @Size(max = 64) String sysUserCode,
        @NotBlank @Size(max = 128) String sysUserName,
        @Size(max = 32) String sysUserMobile,
        @Size(max = 128) String sysUserEmail,
        String avatarFileId,
        @NotNull Integer sysUserStatus) {
}
