package com.fongtaoframework.admin.autoconfigure;

import com.fongtaoframework.admin.application.converter.SysUserConverter;
import com.fongtaoframework.admin.application.converter.impl.SysUserConverterGenerated;
import com.fongtaoframework.admin.web.AdminAuthController;
import com.fongtaoframework.admin.infrastructure.mapper.SysUserMapper;
import com.fongtaoframework.admin.config.AdminProperties;
import com.fongtaoframework.admin.application.AdminAuthService;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfiguration
@MapperScan("com.fongtaoframework.admin.infrastructure.mapper")
@EnableConfigurationProperties(AdminProperties.class)
@ConditionalOnProperty(prefix = "fongtao.admin", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AdminAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SysUserConverter sysUserConverter() {
        return new SysUserConverterGenerated();
    }

    @Bean
    @ConditionalOnMissingBean
    public AdminAuthService adminAuthService(
            SysUserMapper sysUserMapper,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            SysUserConverter sysUserConverter) {
        return new AdminAuthService(sysUserMapper, passwordEncoder, jwtTokenService, sysUserConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public AdminAuthController adminAuthController(AdminAuthService adminAuthService) {
        return new AdminAuthController(adminAuthService);
    }
}
