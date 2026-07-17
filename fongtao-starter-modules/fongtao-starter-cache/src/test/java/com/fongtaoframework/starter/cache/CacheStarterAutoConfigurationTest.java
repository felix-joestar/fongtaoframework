package com.fongtaoframework.starter.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.fongtaoframework.starter.cache.autoconfigure.CacheStarterAutoConfiguration;
import com.fongtaoframework.starter.cache.properties.CacheStarterProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CacheStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CacheStarterAutoConfiguration.class));

    @Test
    void shouldLoadCaffeinePropertiesAndCacheManagerWithoutExternalService() {
        contextRunner.withPropertyValues(
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
    void shouldLoadRedisTemplateAndRedisCacheConfigurationWithoutRealRedisServer() {
        contextRunner
                .withBean(RedisConnectionFactory.class, () -> mock(RedisConnectionFactory.class))
                .withPropertyValues(
                        "fongtao.cache.redis.key-prefix=demo:",
                        "fongtao.cache.redis.default-ttl=30m")
                .run(context -> {
                    assertThat(context).hasSingleBean(CacheStarterProperties.class);
                    assertThat(context).hasSingleBean(RedisCacheConfiguration.class);
                    assertThat(context).hasSingleBean(RedisTemplate.class);
                    assertThat(context.getBean(CacheStarterProperties.class).getRedis().getKeyPrefix())
                            .isEqualTo("demo:");
                });
    }
}
