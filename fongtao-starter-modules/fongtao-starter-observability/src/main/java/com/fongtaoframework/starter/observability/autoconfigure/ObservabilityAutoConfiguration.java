package com.fongtaoframework.starter.observability.autoconfigure;

import com.fongtaoframework.starter.observability.properties.ObservabilityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@ConditionalOnProperty(prefix = "fongtao.observability", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ObservabilityProperties.class)
public class ObservabilityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObservabilitySettings observabilitySettings(ObservabilityProperties properties) {
        return new ObservabilitySettings(
                properties.isArthasEnabled(),
                List.copyOf(properties.getEndpointExposureInclude()));
    }
}
