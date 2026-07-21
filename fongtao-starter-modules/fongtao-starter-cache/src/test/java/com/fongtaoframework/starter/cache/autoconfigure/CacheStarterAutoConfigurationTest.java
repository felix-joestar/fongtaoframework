package com.fongtaoframework.starter.cache.autoconfigure;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.fongtaoframework.starter.cache.properties.CacheStarterProperties;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CacheStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CacheStarterAutoConfiguration.class));

    @Test
    void shouldLoadCaffeinePropertiesAndCacheManagerWithoutExternalService() {
        contextRunner.withBean(RedissonClient.class, () -> mock(RedissonClient.class))
                .withPropertyValues(
                        "fongtao.cache.caffeine.cache-names[0]=demo",
                        "fongtao.cache.caffeine.maximum-size=200",
                        "fongtao.cache.caffeine.expire-after-write=15m")
                .run(context -> {
                    assertThat(context).hasSingleBean(CacheStarterProperties.class);
                    assertThat(context).hasSingleBean(Caffeine.class);
                    assertThat(context).hasSingleBean(CacheManager.class);
                    assertThat(context.getBean(CacheManager.class)).isInstanceOf(CaffeineCacheManager.class);
                    assertThat(context.getBean(CacheStarterProperties.class).getCaffeine().getCacheNames())
                            .containsExactly("demo");
                });
    }

    @Test
    void shouldLoadRedisTemplateAndRedisCacheManagerWithoutRealRedisServer() {
        contextRunner
                .withConfiguration(AutoConfigurations.of(CacheAutoConfiguration.class))
                .withBean(RedisConnectionFactory.class, () -> mock(RedisConnectionFactory.class))
                .withBean(RedissonClient.class, () -> mock(RedissonClient.class))
                .withPropertyValues(
                        "spring.cache.type=redis",
                        "spring.cache.redis.key-prefix=demo:",
                        "spring.cache.redis.time-to-live=30m",
                        "spring.cache.redis.cache-null-values=false")
                .run(context -> {
                    assertThat(context).hasSingleBean(CacheStarterProperties.class);
                    assertThat(context).hasSingleBean(RedisTemplate.class);
                    assertThat(context).hasSingleBean(CacheManager.class);
                    assertThat(context.getBean(CacheManager.class)).isInstanceOf(RedisCacheManager.class);
                });
    }

    @Test
    void shouldBuildRedissonConfigurationFromRedisConnectionDetails() throws Exception {
        RedisConnectionDetails connectionDetails = new RedisConnectionDetails() {

            @Override
            public String getUsername() {
                return "cache-user";
            }

            @Override
            public String getPassword() {
                return "cache-password";
            }

            @Override
            public Standalone getStandalone() {
                return Standalone.of("redis.example.test", 6380, 2);
            }
        };

        Config config = CacheStarterAutoConfiguration.createRedissonConfig(connectionDetails);
        String configuration = config.toYAML();

        assertThat(configuration)
                .contains("address: \"redis://redis.example.test:6380\"")
                .contains("database: 2")
                .contains("username: \"cache-user\"")
                .contains("password: \"cache-password\"");
    }

    @Test
    void shouldIncludeCommonsPoolForLettucePooling() {
        assertThat(ClassUtils.isPresent("org.apache.commons.pool2.impl.GenericObjectPool", getClass().getClassLoader()))
                .isTrue();
    }

}
