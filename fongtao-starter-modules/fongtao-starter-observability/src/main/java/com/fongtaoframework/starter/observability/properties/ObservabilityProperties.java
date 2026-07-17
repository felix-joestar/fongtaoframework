package com.fongtaoframework.starter.observability.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.observability")
public class ObservabilityProperties {

    private boolean enabled = true;

    private boolean arthasEnabled;

    private List<String> endpointExposureInclude = new ArrayList<>(List.of("health", "info"));
}
