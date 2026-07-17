package com.fongtaoframework.starter.mybatis.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fongtaoframework.starter.mybatis.logging.MybatisLog;
import com.fongtaoframework.starter.mybatis.handler.AuditTimeMetaObjectHandler;
import com.fongtaoframework.starter.mybatis.properties.MybatisStarterProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@ConditionalOnClass(MybatisPlusInterceptor.class)
@EnableConfigurationProperties(MybatisStarterProperties.class)
public class MybatisStarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MybatisStarterProperties properties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (properties.isPaginationEnabled()) {
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(properties.getDbType()));
        }
        if (properties.isOptimisticLockerEnabled()) {
            interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        }
        if (properties.isBlockAttackEnabled()) {
            interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        }
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.setLogImpl(MybatisLog.class);
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler auditTimeMetaObjectHandler() {
        return new AuditTimeMetaObjectHandler();
    }

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer(
            MybatisStarterProperties starterProperties,
            Environment environment) {
        return properties -> {
            GlobalConfig globalConfig = globalConfig(properties);
            GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
            if (dbConfig == null) {
                dbConfig = new GlobalConfig.DbConfig();
                globalConfig.setDbConfig(dbConfig);
            }
            if (!hasProperty(environment, "mybatis-plus.global-config.banner")) {
                globalConfig.setBanner(starterProperties.isBanner());
            }
            if (!hasProperty(environment, "mybatis-plus.global-config.db-config.id-type")) {
                dbConfig.setIdType(starterProperties.getIdType());
            }
            if (!hasProperty(environment, "mybatis-plus.global-config.db-config.logic-delete-field")) {
                dbConfig.setLogicDeleteField(starterProperties.getLogicDeleteField());
            }
            if (!hasProperty(environment, "mybatis-plus.global-config.db-config.logic-delete-value")) {
                dbConfig.setLogicDeleteValue(starterProperties.getLogicDeleteValue());
            }
            if (!hasProperty(environment, "mybatis-plus.global-config.db-config.logic-not-delete-value")) {
                dbConfig.setLogicNotDeleteValue(starterProperties.getLogicNotDeleteValue());
            }
            if (!hasProperty(environment, "mybatis-plus.global-config.db-config.update-strategy")) {
                dbConfig.setUpdateStrategy(starterProperties.getUpdateStrategy());
            }
            MybatisPlusProperties.CoreConfiguration configuration = properties.getConfiguration();
            if (configuration == null) {
                configuration = new MybatisPlusProperties.CoreConfiguration();
                properties.setConfiguration(configuration);
            }
            if (!hasProperty(environment, "mybatis-plus.configuration.default-enum-type-handler")) {
                configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
            }
        };
    }

    private GlobalConfig globalConfig(MybatisPlusProperties properties) {
        GlobalConfig globalConfig = properties.getGlobalConfig();
        if (globalConfig == null) {
            globalConfig = new GlobalConfig();
            properties.setGlobalConfig(globalConfig);
        }
        return globalConfig;
    }

    private boolean hasProperty(Environment environment, String propertyName) {
        return environment.containsProperty(propertyName);
    }
}
