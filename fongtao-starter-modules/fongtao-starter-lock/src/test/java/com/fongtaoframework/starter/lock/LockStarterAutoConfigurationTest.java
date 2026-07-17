package com.fongtaoframework.starter.lock;

import com.fongtaoframework.starter.lock.autoconfigure.LockStarterAutoConfiguration;
import com.fongtaoframework.starter.lock.properties.LockStarterProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class LockStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LockStarterAutoConfiguration.class));

    @Test
    void shouldBindLockProperties() {
        contextRunner.withPropertyValues(
                        "fongtao.lock.default-acquire-timeout=5s",
                        "fongtao.lock.default-expire=45s")
                .run(context -> {
                    assertThat(context).hasSingleBean(LockStarterProperties.class);
                    LockStarterProperties properties = context.getBean(LockStarterProperties.class);
                    assertThat(properties.getDefaultAcquireTimeout()).isEqualTo(Duration.ofSeconds(5));
                    assertThat(properties.getDefaultExpire()).isEqualTo(Duration.ofSeconds(45));
                });
    }
}
