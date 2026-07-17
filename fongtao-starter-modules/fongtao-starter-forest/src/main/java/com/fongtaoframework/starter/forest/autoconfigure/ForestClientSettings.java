package com.fongtaoframework.starter.forest.autoconfigure;

import java.time.Duration;
import java.util.List;

public record ForestClientSettings(
        Duration connectTimeout,
        Duration readTimeout,
        boolean logEnabled,
        List<String> basePackages) {
}
