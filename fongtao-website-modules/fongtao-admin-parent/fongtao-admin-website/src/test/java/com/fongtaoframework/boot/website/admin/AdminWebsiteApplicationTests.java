package com.fongtaoframework.boot.website.admin;

import org.redisson.api.RedissonClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.mockito.Mockito.mock;

@SpringBootTest(properties = {
        "fongtao.admin.enabled=false",
        "fongtao.security.jwt.secret=0123456789abcdef0123456789abcdef",
        "spring.cache.type=none",
        "spring.autoconfigure.exclude=com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
@Import(AdminWebsiteApplicationTests.RedisTestConfiguration.class)
class AdminWebsiteApplicationTests {

    @Test
    void contextLoads() {
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class RedisTestConfiguration {

        @Bean
        RedisConnectionFactory redisConnectionFactory() {
            return mock(RedisConnectionFactory.class);
        }

        @Bean
        RedissonClient redissonClient() {
            return mock(RedissonClient.class);
        }
    }
}
