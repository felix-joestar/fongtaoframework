package com.fongtaoframework.starter.security;

import com.fongtaoframework.starter.security.properties.SecurityStarterProperties;
import com.fongtaoframework.starter.security.properties.SecurityStarterPropertiesValidator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityProductionSafetyTest {

    @Test
    void prodShouldRejectDefaultJwtSecret() {
        SecurityStarterProperties properties = new SecurityStarterProperties();
        properties.getCors().setAllowedOriginPatterns(List.of("https://admin.example.com"));
        MockEnvironment environment = new MockEnvironment().withProperty("spring.profiles.active", "prod");
        environment.setActiveProfiles("prod");

        assertThatThrownBy(() -> SecurityStarterPropertiesValidator.validate(properties, environment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("fongtao.security.jwt.secret");
    }

    @Test
    void prodShouldRejectWildcardCors() {
        SecurityStarterProperties properties = new SecurityStarterProperties();
        properties.getJwt().setSecret("0123456789abcdef0123456789abcdef");
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("production");

        assertThatThrownBy(() -> SecurityStarterPropertiesValidator.validate(properties, environment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("fongtao.security.cors.allowed-origin-patterns");
    }

    @Test
    void devShouldAllowLocalDefaults() {
        SecurityStarterProperties properties = new SecurityStarterProperties();
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("dev");

        assertThatCode(() -> SecurityStarterPropertiesValidator.validate(properties, environment))
                .doesNotThrowAnyException();
    }
}
