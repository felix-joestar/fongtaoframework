package com.fongtaoframework.starter.observability;

import com.fongtaoframework.starter.observability.env.ObservabilityDefaultsPostProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class ObservabilityStarterAutoConfigurationTest {

    @Test
    void environmentDefaultsShouldDisableArthasNativeProperty() {
        MockEnvironment environment = new MockEnvironment();

        new ObservabilityDefaultsPostProcessor()
                .postProcessEnvironment(environment, new SpringApplication(ObservabilityStarterAutoConfigurationTest.class));

        assertThat(environment.getProperty("arthas.enabled")).isEqualTo("false");
        assertThat(environment.getProperty("management.endpoints.web.exposure.include")).isEqualTo("health,info");
    }
}
