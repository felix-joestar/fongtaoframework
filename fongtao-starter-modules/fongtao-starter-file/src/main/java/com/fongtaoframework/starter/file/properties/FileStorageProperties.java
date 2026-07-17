package com.fongtaoframework.starter.file.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.file")
public class FileStorageProperties {

    public static final String DEFAULT_LOCAL_BASE_PATH = "data/fongtao-files";

    private boolean enabled = true;

    private String storageType = "local";

    private String localBasePath = DEFAULT_LOCAL_BASE_PATH;
}
