package com.fongtaoframework.starter.admin.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.admin")
public class AdminProperties {

    private boolean enabled = true;
}
