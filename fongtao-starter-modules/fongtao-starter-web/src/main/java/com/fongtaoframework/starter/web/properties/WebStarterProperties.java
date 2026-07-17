package com.fongtaoframework.starter.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "fongtao.web")
public class WebStarterProperties {

    private final Trace trace = new Trace();

    @Getter
    @Setter
    public static class Trace {

        private boolean enabled = true;

        private String headerName = "X-Trace-Id";

        private String mdcName = "traceId";
    }
}
