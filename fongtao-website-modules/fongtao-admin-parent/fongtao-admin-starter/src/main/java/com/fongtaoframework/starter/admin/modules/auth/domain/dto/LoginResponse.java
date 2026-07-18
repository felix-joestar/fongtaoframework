package com.fongtaoframework.starter.admin.modules.auth.domain.dto;

public record LoginResponse(
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn,
        LoginUserResponse user) {
}
