package com.fongtaoframework.starter.file;

import com.fongtaoframework.starter.file.autoconfigure.FileStorageAutoConfiguration;
import com.fongtaoframework.starter.file.autoconfigure.FileStorageSettings;
import com.fongtaoframework.starter.file.properties.FileStorageProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class FileStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(FileStorageAutoConfiguration.class));

    @Test
    void shouldUseLocalStorageByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(FileStorageProperties.class);
            assertThat(context).hasSingleBean(FileStorageSettings.class);
            assertThat(context.getBean(FileStorageSettings.class).localBasePath())
                    .isEqualTo(FileStorageProperties.DEFAULT_LOCAL_BASE_PATH);
        });
    }

    @Test
    void prodShouldRequireExplicitLocalPath() {
        contextRunner.withInitializer(context -> context.getEnvironment().setActiveProfiles("prod"))
                .run(context -> assertThat(context).hasFailed());
    }
}
