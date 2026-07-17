package com.fongtaoframework.admin.web.dto;

import java.util.List;

public record LoginUserResponse(
        String userId,
        String username,
        String name,
        String mobile,
        String email,
        String avatarFileId,
        List<String> permissions) {}
