package com.fongtaoframework.starter.security.autoconfigure;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import com.fongtaoframework.starter.security.properties.SecurityStarterProperties;
import com.fongtaoframework.starter.security.properties.SecurityStarterPropertiesValidator;
import com.fongtaoframework.starter.security.web.BearerTokenAuthenticationFilter;
import com.fongtaoframework.starter.security.web.JsonAccessDeniedHandler;
import com.fongtaoframework.starter.security.web.JsonAuthenticationEntryPoint;
import com.fongtaoframework.starter.security.web.MethodSecurityExceptionHandler;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@AutoConfiguration
@EnableMethodSecurity
@ConditionalOnClass(SecurityFilterChain.class)
@EnableConfigurationProperties(SecurityStarterProperties.class)
@ConditionalOnProperty(prefix = "fongtao.security", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityStarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenService jwtTokenService(SecurityStarterProperties properties, Environment environment) {
        SecurityStarterPropertiesValidator.validate(properties, environment);
        return new JwtTokenService(properties);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JsonAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public JsonAccessDeniedHandler jsonAccessDeniedHandler(ObjectMapper objectMapper) {
        return new JsonAccessDeniedHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodSecurityExceptionHandler methodSecurityExceptionHandler() {
        return new MethodSecurityExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsConfigurationSource corsConfigurationSource(
            SecurityStarterProperties properties,
            Environment environment) {
        SecurityStarterPropertiesValidator.validate(properties, environment);
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(properties.getCors().getAllowedOriginPatterns());
        configuration.setAllowedMethods(properties.getCors().getAllowedMethods());
        configuration.setAllowedHeaders(properties.getCors().getAllowedHeaders());
        configuration.setExposedHeaders(properties.getCors().getExposedHeaders());
        configuration.setAllowCredentials(properties.getCors().isAllowCredentials());
        configuration.setMaxAge(properties.getCors().getMaxAge());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityStarterProperties properties,
            JwtTokenService jwtTokenService,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler) throws Exception {
        BearerTokenAuthenticationFilter bearerTokenFilter =
                new BearerTokenAuthenticationFilter(jwtTokenService, authenticationEntryPoint);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    List<String> permitPaths = properties.getPermitPaths();
                    if (CollUtil.isNotEmpty(permitPaths)) {
                        authorize.requestMatchers(permitPaths.toArray(String[]::new)).permitAll();
                    }
                    authorize.anyRequest().authenticated();
                })
                .addFilterBefore(bearerTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
