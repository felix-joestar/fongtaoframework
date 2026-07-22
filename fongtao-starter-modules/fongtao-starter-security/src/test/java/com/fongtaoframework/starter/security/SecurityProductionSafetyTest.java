package com.fongtaoframework.starter.security;

import com.fongtaoframework.starter.security.properties.SecurityStarterProperties;
import com.fongtaoframework.starter.security.properties.SecurityStarterPropertiesValidator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityProductionSafetyTest {

    @Test
    void shouldRejectMissingJwtSecret() {
        SecurityStarterProperties properties = new SecurityStarterProperties();

        assertThatThrownBy(() -> SecurityStarterPropertiesValidator.validate(properties, new MockEnvironment()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("fongtao.security.jwt.secret");
    }

    @Test
    void shouldAllowExplicitJwtSecret() {
        SecurityStarterProperties properties = new SecurityStarterProperties();
        properties.getJwt().setSecret("0123456789abcdef0123456789abcdef");

        assertThatCode(() -> SecurityStarterPropertiesValidator.validate(properties, new MockEnvironment()))
                .doesNotThrowAnyException();
    }
}
