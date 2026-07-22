package com.fongtaoframework.starter.security.properties;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.env.Environment;

public final class SecurityStarterPropertiesValidator {

    private SecurityStarterPropertiesValidator() {
    }

    public static void validate(SecurityStarterProperties properties, Environment environment) {
        validateJwtSecret(properties);
    }

    private static void validateJwtSecret(SecurityStarterProperties properties) {
        String secret = properties.getJwt().getSecret();
        if (StrUtil.isBlank(secret)) {
            throw new IllegalStateException("必须显式配置 fongtao.security.jwt.secret");
        }
    }
}
