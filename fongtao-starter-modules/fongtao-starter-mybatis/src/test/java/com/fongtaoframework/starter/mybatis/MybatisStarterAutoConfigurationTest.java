package com.fongtaoframework.starter.mybatis;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fongtaoframework.starter.mybatis.autoconfigure.MybatisStarterAutoConfiguration;
import com.fongtaoframework.starter.mybatis.handler.AuditTimeMetaObjectHandler;
import com.fongtaoframework.starter.mybatis.logging.MybatisLog;
import com.fongtaoframework.starter.mybatis.properties.MybatisStarterProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class MybatisStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MybatisStarterAutoConfiguration.class));

    @Test
    void shouldLoadDefaultMybatisStarterBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MybatisStarterProperties.class);
            assertThat(context).hasSingleBean(MybatisPlusInterceptor.class);
            assertThat(context).hasSingleBean(ConfigurationCustomizer.class);
            assertThat(context).hasSingleBean(MybatisPlusPropertiesCustomizer.class);
            assertThat(context).hasSingleBean(MetaObjectHandler.class);
            assertThat(context.getBean(MetaObjectHandler.class)).isInstanceOf(AuditTimeMetaObjectHandler.class);

            MybatisPlusInterceptor interceptor = context.getBean(MybatisPlusInterceptor.class);
            assertThat(interceptor.getInterceptors())
                    .hasExactlyElementsOfTypes(
                            PaginationInnerInterceptor.class,
                            OptimisticLockerInnerInterceptor.class,
                            BlockAttackInnerInterceptor.class);
        });
    }

    @Test
    void shouldAllowDisablingOptionalInnerInterceptors() {
        contextRunner.withPropertyValues(
                        "fongtao.mybatis.optimistic-locker-enabled=false",
                        "fongtao.mybatis.block-attack-enabled=false")
                .run(context -> {
                    MybatisPlusInterceptor interceptor = context.getBean(MybatisPlusInterceptor.class);
                    assertThat(interceptor.getInterceptors())
                            .hasExactlyElementsOfTypes(PaginationInnerInterceptor.class);
                });
    }

    @Test
    void shouldApplyMybatisPlusGlobalDefaults() {
        contextRunner.run(context -> {
            MybatisPlusProperties properties = new MybatisPlusProperties();
            context.getBean(MybatisPlusPropertiesCustomizer.class).customize(properties);

            assertThat(properties.getGlobalConfig().isBanner()).isFalse();
            assertThat(properties.getGlobalConfig().getDbConfig().getIdType()).isEqualTo(IdType.ASSIGN_UUID);
            assertThat(properties.getGlobalConfig().getDbConfig().getLogicDeleteField()).isEqualTo("deleted");
            assertThat(properties.getGlobalConfig().getDbConfig().getLogicDeleteValue()).isEqualTo("1");
            assertThat(properties.getGlobalConfig().getDbConfig().getLogicNotDeleteValue()).isEqualTo("0");
            assertThat(properties.getGlobalConfig().getDbConfig().getUpdateStrategy()).isEqualTo(FieldStrategy.ALWAYS);
            assertThat(properties.getConfiguration().getDefaultEnumTypeHandler())
                    .isEqualTo(MybatisEnumTypeHandler.class);
        });
    }

    @Test
    void shouldApplyMybatisConfigurationDefaults() {
        contextRunner.run(context -> {
            MybatisConfiguration configuration = new MybatisConfiguration();
            context.getBean(ConfigurationCustomizer.class).customize(configuration);

            assertThat(configuration.getLogImpl()).isEqualTo(MybatisLog.class);
        });
    }
}
