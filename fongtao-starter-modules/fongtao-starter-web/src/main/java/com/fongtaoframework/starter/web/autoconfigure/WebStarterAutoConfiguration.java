package com.fongtaoframework.starter.web.autoconfigure;

import com.fongtaoframework.starter.web.exception.GlobalExceptionHandler;
import com.fongtaoframework.starter.web.properties.WebStarterProperties;
import com.fongtaoframework.starter.web.trace.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AutoConfiguration
@ConditionalOnClass(RestControllerAdvice.class)
@EnableConfigurationProperties(WebStarterProperties.class)
@Import(GlobalExceptionHandler.class)
public class WebStarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fongtao.web.trace", name = "enabled", havingValue = "true", matchIfMissing = true)
    public TraceIdFilter traceIdFilter(WebStarterProperties properties) {
        return new TraceIdFilter(properties.getTrace());
    }
}
