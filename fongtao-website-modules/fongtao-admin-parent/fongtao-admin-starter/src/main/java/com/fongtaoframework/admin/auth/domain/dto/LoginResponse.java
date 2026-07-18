package com.fongtaoframework.admin.auth.domain.dto;

public record LoginResponse(
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn,
        LoginUserResponse user) {
}
