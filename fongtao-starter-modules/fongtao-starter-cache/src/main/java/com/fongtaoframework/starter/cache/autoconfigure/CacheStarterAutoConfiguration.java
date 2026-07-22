package com.fongtaoframework.starter.cache.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.fongtaoframework.starter.cache.properties.CacheStarterProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

@AutoConfiguration
@EnableCaching
@EnableConfigurationProperties(CacheStarterProperties.class)
public class CacheStarterAutoConfiguration {

    @Bean
    @ConditionalOnClass(CaffeineCacheManager.class)
    @ConditionalOnExpression("${fongtao.cache.caffeine.enabled:true} && ('${spring.cache.type:}' == '' || '${spring.cache.type:}' == 'caffeine')")
    @ConditionalOnMissingBean(CacheManager.class)
    public CaffeineCacheManager caffeineCacheManager(CacheStarterProperties properties) {
        CacheStarterProperties.Caffeine caffeine = properties.getCaffeine();
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .maximumSize(caffeine.getMaximumSize())
                .expireAfterWrite(caffeine.getExpireAfterWrite());
        if (caffeine.isRecordStats()) {
            builder.recordStats();
        }
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(builder);
        cacheManager.setAllowNullValues(caffeine.isAllowNullValues());
        if (!caffeine.getCacheNames().isEmpty()) {
            cacheManager.setCacheNames(caffeine.getCacheNames());
        }
        return cacheManager;
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnClass(RedissonClient.class)
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedisConnectionDetails connectionDetails) {
        return Redisson.create(createRedissonConfig(connectionDetails));
    }

    static Config createRedissonConfig(RedisConnectionDetails connectionDetails) {
        RedisConnectionDetails.Standalone standalone = connectionDetails.getStandalone();
        Config config = new Config();
        SingleServerConfig singleServer = config.useSingleServer()
                .setAddress("redis://" + standalone.getHost() + ":" + standalone.getPort())
                .setDatabase(standalone.getDatabase());
        if (StringUtils.hasText(connectionDetails.getUsername())) {
            singleServer.setUsername(connectionDetails.getUsername());
        }
        if (StringUtils.hasText(connectionDetails.getPassword())) {
            singleServer.setPassword(connectionDetails.getPassword());
        }
        return config;
    }

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            ObjectProvider<ObjectMapper> objectMapperProvider) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        GenericJackson2JsonRedisSerializer valueSerializer =
                new GenericJackson2JsonRedisSerializer(resolveObjectMapper(objectMapperProvider));
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private static ObjectMapper resolveObjectMapper(ObjectProvider<ObjectMapper> objectMapperProvider) {
        return objectMapperProvider.getIfAvailable(ObjectMapper::new);
    }
}
