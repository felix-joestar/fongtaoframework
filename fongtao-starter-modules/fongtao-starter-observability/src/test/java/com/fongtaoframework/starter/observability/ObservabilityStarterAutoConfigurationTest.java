package com.fongtaoframework.starter.observability;

import com.fongtaoframework.starter.observability.autoconfigure.ObservabilityAutoConfiguration;
import com.fongtaoframework.starter.observability.autoconfigure.ObservabilitySettings;
import com.fongtaoframework.starter.observability.env.ObservabilityDefaultsPostProcessor;
import com.fongtaoframework.starter.observability.properties.ObservabilityProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class ObservabilityStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ObservabilityAutoConfiguration.class));

    @Test
    void shouldDisableArthasByDefaultInObservabilityProperties() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ObservabilityProperties.class);
            ObservabilitySettings settings = context.getBean(ObservabilitySettings.class);
            assertThat(settings.arthasEnabled()).isFalse();
            assertThat(settings.endpointExposureInclude()).containsExactly("health", "info");
        });
    }

    @Test
    void environmentDefaultsShouldDisableArthasNativeProperty() {
        MockEnvironment environment = new MockEnvironment();

        new ObservabilityDefaultsPostProcessor()
                .postProcessEnvironment(environment, new SpringApplication(ObservabilityStarterAutoConfigurationTest.class));

        assertThat(environment.getProperty("arthas.enabled")).isEqualTo("false");
        assertThat(environment.getProperty("management.endpoints.web.exposure.include")).isEqualTo("health,info");
    }
}
