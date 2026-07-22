package com.fongtaoframework.starter.admin.modules.rights.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record SysUserRow(
        String sysUserId,
        String sysUserCode,
        String sysUserName,
        String sysUserMobile,
        String sysUserEmail,
        String avatarFileId,
        Integer sysUserStatus,
        Long version
) {
}
