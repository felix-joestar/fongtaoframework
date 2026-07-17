package com.fongtaoframework.starter.forest;

import com.fongtaoframework.starter.forest.autoconfigure.ForestClientAutoConfiguration;
import com.fongtaoframework.starter.forest.autoconfigure.ForestClientSettings;
import com.fongtaoframework.starter.forest.properties.ForestClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class ForestStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ForestClientAutoConfiguration.class));

    @Test
    void shouldBindForestProperties() {
        contextRunner.withPropertyValues(
                        "fongtao.forest.connect-timeout=2s",
                        "fongtao.forest.read-timeout=8s",
                        "fongtao.forest.log-enabled=true",
                        "fongtao.forest.base-packages[0]=com.example.client")
                .run(context -> {
                    assertThat(context).hasSingleBean(ForestClientProperties.class);
                    ForestClientSettings settings = context.getBean(ForestClientSettings.class);
                    assertThat(settings.connectTimeout()).isEqualTo(Duration.ofSeconds(2));
                    assertThat(settings.readTimeout()).isEqualTo(Duration.ofSeconds(8));
                    assertThat(settings.logEnabled()).isTrue();
                    assertThat(settings.basePackages()).containsExactly("com.example.client");
                });
    }
}
