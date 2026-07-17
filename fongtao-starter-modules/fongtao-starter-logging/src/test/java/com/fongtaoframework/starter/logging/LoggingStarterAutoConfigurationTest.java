package com.fongtaoframework.starter.logging;

import com.fongtaoframework.starter.logging.autoconfigure.LoggingStarterAutoConfiguration;
import com.fongtaoframework.starter.logging.properties.LoggingStarterProperties;
import com.fongtaoframework.starter.logging.support.RequestIdResolver;
import com.fongtaoframework.starter.logging.support.SensitiveDataSanitizer;
import com.fongtaoframework.starter.logging.web.RequestLoggingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LoggingStarterAutoConfiguration.class));

    @Test
    void shouldMaskSensitiveHeadersQueryJsonAndFormFields() {
        String header = "Authorization: Bearer abc";
        String line = "/api?token=abc&name=demo {\"password\":\"123\",\"email\":\"a@b.com\"} mobile=13800138000";

        String maskedHeader = SensitiveDataSanitizer.maskLine(header);
        String masked = SensitiveDataSanitizer.maskLine(line);

        assertThat(maskedHeader).isEqualTo("Authorization: ******");
        assertThat(masked).doesNotContain("Bearer abc", "token=abc", "123", "a@b.com", "13800138000");
        assertThat(masked).contains("token=******", "\"password\":\"******\"", "\"email\":\"******\"", "mobile=******");
    }

    @Test
    void requestLoggingFilterShouldBeOptIn() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(LoggingStarterProperties.class);
            assertThat(context).hasSingleBean(RequestIdResolver.class);
            assertThat(context).doesNotHaveBean(RequestLoggingFilter.class);
        });
    }

    @Test
    void requestLoggingFilterShouldLoadWhenEnabled() {
        contextRunner.withPropertyValues("fongtao.logging.request.enabled=true")
                .run(context -> assertThat(context).hasSingleBean(RequestLoggingFilter.class));
    }
}
