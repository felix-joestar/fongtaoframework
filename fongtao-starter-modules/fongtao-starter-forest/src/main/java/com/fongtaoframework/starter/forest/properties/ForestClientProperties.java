package com.fongtaoframework.starter.forest.properties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.forest")
public class ForestClientProperties {

    private boolean enabled = true;

    private Duration connectTimeout = Duration.ofSeconds(3);

    private Duration readTimeout = Duration.ofSeconds(10);

    private boolean logEnabled;

    private List<String> basePackages = new ArrayList<>();
}
