package com.fongtaoframework.admin.web.dto;

public record LoginResponse(
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn,
        LoginUserResponse user) {
}
