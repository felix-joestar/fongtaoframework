package com.fongtaoframework.starter.security.properties;

import cn.hutool.core.util.StrUtil;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.core.env.Environment;

public final class SecurityStarterPropertiesValidator {

    private SecurityStarterPropertiesValidator() {
    }

    public static void validate(SecurityStarterProperties properties, Environment environment) {
        if (!isProduction(environment)) {
            return;
        }
        validateJwtSecret(properties);
        validateCors(properties);
    }

    private static boolean isProduction(Environment environment) {
        return Arrays.stream(environment.getActiveProfiles())
                .map(profile -> profile.toLowerCase(Locale.ROOT))
                .anyMatch(profile -> "prod".equals(profile) || "production".equals(profile));
    }

    private static void validateJwtSecret(SecurityStarterProperties properties) {
        String secret = properties.getJwt().getSecret();
        if (StrUtil.isBlank(secret)
                || SecurityStarterProperties.DEFAULT_JWT_SECRET.equals(secret)) {
            throw new IllegalStateException("生产环境必须显式配置 fongtao.security.jwt.secret，禁止使用默认 JWT secret");
        }
    }

    private static void validateCors(SecurityStarterProperties properties) {
        boolean hasWildcard = properties.getCors().getAllowedOriginPatterns().stream()
                .anyMatch(pattern -> "*".equals(pattern));
        if (hasWildcard) {
            throw new IllegalStateException(
                    "生产环境必须显式配置 fongtao.security.cors.allowed-origin-patterns，禁止使用 *");
        }
    }
}
