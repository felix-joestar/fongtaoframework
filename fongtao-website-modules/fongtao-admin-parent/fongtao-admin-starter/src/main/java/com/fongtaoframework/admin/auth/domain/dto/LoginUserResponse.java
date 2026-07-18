package com.fongtaoframework.admin.auth.domain.dto;

import java.util.List;

public record LoginUserResponse(
        String userId,
        String username,
        String name,
        String mobile,
        String email,
        String avatarFileId,
        List<String> permissions) {}
