package com.fongtaoframework.starter.admin.common.autoconfigure;

import com.fongtaoframework.starter.admin.common.properties.AdminProperties;
import com.fongtaoframework.starter.mybatis.autoconfigure.MybatisStarterAutoConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Admin starter auto-configuration entry point.
 */
@AutoConfiguration(before = MybatisStarterAutoConfiguration.class)
@ComponentScan(basePackages = "com.fongtaoframework.starter.admin.modules")
@MapperScan(
        basePackages = "com.fongtaoframework.starter.admin.modules",
        annotationClass = Mapper.class
)
@EnableConfigurationProperties(AdminProperties.class)
@ConditionalOnProperty(prefix = "fongtao.admin", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AdminAutoConfiguration {
}
