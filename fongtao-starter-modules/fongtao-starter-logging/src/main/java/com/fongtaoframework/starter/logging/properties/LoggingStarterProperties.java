package com.fongtaoframework.starter.logging.properties;

import com.fongtaoframework.starter.logging.constants.LogConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "fongtao.logging")
public class LoggingStarterProperties {

    private final Request request = new Request();

    @Getter
    @Setter
    public static class Request {

        private boolean enabled;

        private String traceHeaderName = LogConstants.TRACE_ID_HEADER;

        private String traceMdcName = LogConstants.MDC_TRACE_ID;
    }
}
