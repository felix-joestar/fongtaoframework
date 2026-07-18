package com.fongtaoframework.starter.admin.common.autoconfigure;

import com.fongtaoframework.starter.admin.modules.auth.converter.SysUserConverter;
import com.fongtaoframework.starter.admin.modules.auth.converter.impl.SysUserConverterGenerated;
import com.fongtaoframework.starter.admin.modules.auth.controller.AdminAuthController;
import com.fongtaoframework.starter.admin.modules.auth.facade.IAdminAuthFacade;
import com.fongtaoframework.starter.admin.modules.auth.facade.impl.AdminAuthFacade;
import com.fongtaoframework.starter.admin.modules.auth.mapper.SysUserMapper;
import com.fongtaoframework.starter.admin.modules.auth.service.ISysUserService;
import com.fongtaoframework.starter.admin.modules.auth.service.impl.SysUserService;
import com.fongtaoframework.starter.admin.common.properties.AdminProperties;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfiguration
@MapperScan("com.fongtaoframework.starter.admin.modules.auth.mapper")
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
    public ISysUserService sysUserService(SysUserMapper sysUserMapper) {
        return new SysUserService(sysUserMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public IAdminAuthFacade adminAuthFacade(
            ISysUserService sysUserService,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            SysUserConverter sysUserConverter) {
        return new AdminAuthFacade(sysUserService, passwordEncoder, jwtTokenService, sysUserConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public AdminAuthController adminAuthController(IAdminAuthFacade adminAuthFacade) {
        return new AdminAuthController(adminAuthFacade);
    }
}
