package com.fongtaoframework.starter.forest.autoconfigure;

import com.fongtaoframework.starter.forest.properties.ForestClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@ConditionalOnProperty(prefix = "fongtao.forest", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ForestClientProperties.class)
public class ForestClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ForestClientSettings forestClientSettings(ForestClientProperties properties) {
        return new ForestClientSettings(
                properties.getConnectTimeout(),
                properties.getReadTimeout(),
                properties.isLogEnabled(),
                List.copyOf(properties.getBasePackages()));
    }
}
