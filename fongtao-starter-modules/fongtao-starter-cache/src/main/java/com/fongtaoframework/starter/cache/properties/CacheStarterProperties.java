package com.fongtaoframework.starter.cache.properties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.cache")
public class CacheStarterProperties {

    private final Caffeine caffeine = new Caffeine();

    private final Redis redis = new Redis();

    @Getter
    @Setter
    public static class Caffeine {

        private boolean enabled = true;

        private List<String> cacheNames = new ArrayList<>();

        private long maximumSize = 1000;

        private Duration expireAfterWrite = Duration.ofMinutes(30);

        private boolean allowNullValues;

        private boolean recordStats;
    }

    @Getter
    @Setter
    public static class Redis {

        private boolean enabled = true;

        private String keyPrefix = "fongtao:";

        private Duration defaultTtl = Duration.ofHours(1);

        private boolean cacheNullValues;
    }
}
