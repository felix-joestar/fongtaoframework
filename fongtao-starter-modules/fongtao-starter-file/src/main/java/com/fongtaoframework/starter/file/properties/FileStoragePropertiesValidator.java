package com.fongtaoframework.starter.file.properties;

import cn.hutool.core.util.StrUtil;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.core.env.Environment;

public final class FileStoragePropertiesValidator {

    private FileStoragePropertiesValidator() {
    }

    public static void validate(FileStorageProperties properties, Environment environment) {
        if (!isProduction(environment)) {
            return;
        }
        if (StrUtil.equalsIgnoreCase("local", properties.getStorageType())
                && StrUtil.equals(FileStorageProperties.DEFAULT_LOCAL_BASE_PATH, properties.getLocalBasePath())) {
            throw new IllegalStateException("生产环境必须显式配置 fongtao.file.local-base-path");
        }
    }

    private static boolean isProduction(Environment environment) {
        return Arrays.stream(environment.getActiveProfiles())
                .map(profile -> profile.toLowerCase(Locale.ROOT))
                .anyMatch(profile -> "prod".equals(profile) || "production".equals(profile));
    }
}
