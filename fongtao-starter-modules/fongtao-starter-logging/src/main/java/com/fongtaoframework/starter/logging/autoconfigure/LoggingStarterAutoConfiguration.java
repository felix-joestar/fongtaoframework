package com.fongtaoframework.starter.logging.autoconfigure;

import com.fongtaoframework.starter.logging.properties.LoggingStarterProperties;
import com.fongtaoframework.starter.logging.support.RequestIdResolver;
import com.fongtaoframework.starter.logging.web.RequestLoggingFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

@AutoConfiguration
@EnableConfigurationProperties(LoggingStarterProperties.class)
public class LoggingStarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RequestIdResolver requestIdResolver() {
        return new RequestIdResolver();
    }

    @Bean
    @ConditionalOnClass(OncePerRequestFilter.class)
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fongtao.logging.request", name = "enabled", havingValue = "true")
    public RequestLoggingFilter requestLoggingFilter(
            LoggingStarterProperties properties,
            RequestIdResolver requestIdResolver) {
        return new RequestLoggingFilter(properties.getRequest(), requestIdResolver);
    }
}
