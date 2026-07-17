package com.fongtaoframework.starter.cache.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.fongtaoframework.starter.cache.properties.CacheStarterProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@AutoConfiguration
@EnableConfigurationProperties(CacheStarterProperties.class)
public class CacheStarterAutoConfiguration {

    @Bean
    @ConditionalOnClass(Caffeine.class)
    @ConditionalOnProperty(prefix = "fongtao.cache.caffeine", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public Caffeine<Object, Object> caffeine(CacheStarterProperties properties) {
        CacheStarterProperties.Caffeine caffeine = properties.getCaffeine();
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .maximumSize(caffeine.getMaximumSize())
                .expireAfterWrite(caffeine.getExpireAfterWrite());
        if (caffeine.isRecordStats()) {
            builder.recordStats();
        }
        return builder;
    }

    @Bean
    @ConditionalOnClass(CaffeineCacheManager.class)
    @ConditionalOnProperty(prefix = "fongtao.cache.caffeine", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(CacheManager.class)
    public CaffeineCacheManager caffeineCacheManager(
            Caffeine<Object, Object> caffeine,
            CacheStarterProperties properties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        cacheManager.setAllowNullValues(properties.getCaffeine().isAllowNullValues());
        if (!properties.getCaffeine().getCacheNames().isEmpty()) {
            cacheManager.setCacheNames(properties.getCaffeine().getCacheNames());
        }
        return cacheManager;
    }

    @Bean
    @ConditionalOnClass(RedisCacheConfiguration.class)
    @ConditionalOnProperty(prefix = "fongtao.cache.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public RedisCacheConfiguration redisCacheConfiguration(
            CacheStarterProperties properties,
            ObjectProvider<ObjectMapper> objectMapperProvider) {
        CacheStarterProperties.Redis redis = properties.getRedis();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(redis.getDefaultTtl())
                .prefixCacheNameWith(redis.getKeyPrefix())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(resolveObjectMapper(objectMapperProvider))));
        if (!redis.isCacheNullValues()) {
            configuration = configuration.disableCachingNullValues();
        }
        return configuration;
    }

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(prefix = "fongtao.cache.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
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
