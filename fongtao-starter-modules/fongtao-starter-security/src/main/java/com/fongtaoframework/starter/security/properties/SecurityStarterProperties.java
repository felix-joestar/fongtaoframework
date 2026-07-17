package com.fongtaoframework.starter.security.properties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fongtao.security")
public class SecurityStarterProperties {

    public static final String DEFAULT_JWT_SECRET = "change-this-fongtao-jwt-secret-32bytes";

    private boolean enabled = true;
    private List<String> permitPaths = new ArrayList<>(List.of(
            "/auth/login",
            "/auth/refresh-token",
            "/actuator/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"));
    private Jwt jwt = new Jwt();
    private Cors cors = new Cors();

    @Getter
    @Setter
    public static class Jwt {

        private String issuer = "fongtao";
        private String secret = DEFAULT_JWT_SECRET;
        private Duration accessTokenTtl = Duration.ofMinutes(30);
        private Duration refreshTokenTtl = Duration.ofDays(7);
    }

    @Getter
    @Setter
    public static class Cors {

        private List<String> allowedOriginPatterns = new ArrayList<>(List.of("*"));
        private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        private List<String> allowedHeaders = new ArrayList<>(List.of("*"));
        private List<String> exposedHeaders = new ArrayList<>(List.of("Authorization"));
        private boolean allowCredentials;
        private Long maxAge = 1800L;
    }
}
