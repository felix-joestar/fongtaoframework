package com.fongtaoframework.starter.file.autoconfigure;

import com.fongtaoframework.starter.file.properties.FileStorageProperties;
import com.fongtaoframework.starter.file.properties.FileStoragePropertiesValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@ConditionalOnProperty(prefix = "fongtao.file", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileStorageSettings fileStorageSettings(
            FileStorageProperties properties,
            Environment environment) {
        FileStoragePropertiesValidator.validate(properties, environment);
        return new FileStorageSettings(properties.getStorageType(), properties.getLocalBasePath());
    }
}
