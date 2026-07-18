package com.fongtaoframework.starter.admin.modules.auth.domain.dto;

import java.util.List;

public record LoginUserResponse(
        String userId,
        String username,
        String name,
        String mobile,
        String email,
        String avatarFileId,
        List<String> permissions) {}
