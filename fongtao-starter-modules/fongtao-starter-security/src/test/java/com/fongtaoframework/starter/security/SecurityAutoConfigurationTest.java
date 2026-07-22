package com.fongtaoframework.starter.security;

import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import com.fongtaoframework.starter.security.jwt.TokenValue;
import com.fongtaoframework.starter.security.userdetails.LoginUserDetails;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = SecurityAutoConfigurationTest.TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {
                "fongtao.security.jwt.secret=0123456789abcdef0123456789abcdef",
                "fongtao.security.jwt.access-token-ttl=60s",
                "fongtao.security.jwt.refresh-token-ttl=300s",
                "fongtao.security.permit-paths=/auth/login,/actuator/health"
        })
@AutoConfigureMockMvc
class SecurityAutoConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    void permitPathShouldBeAccessibleWithoutBearerToken() throws Exception {
        mockMvc.perform(post("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void protectedPathShouldReturnJson401WithoutBearerToken() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void protectedPathShouldAcceptValidBearerToken() throws Exception {
        LoginUserDetails loginUser = new LoginUserDetails(
                "u1",
                "admin",
                "管理员",
                "13800000000",
                "admin@example.com",
                null,
                List.of(new SimpleGrantedAuthority("login")));
        String accessToken = jwtTokenService.createAccessToken(loginUser).token();

        mockMvc.perform(get("/private").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("ok"));
    }

    @Test
    void invalidBearerTokenShouldReturnJson401() throws Exception {
        mockMvc.perform(get("/private").header(HttpHeaders.AUTHORIZATION, "Bearer invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", not(blankOrNullString())))
                .andExpect(jsonPath("$.traceId", not(blankOrNullString())));
    }

    @Test
    void refreshTokenShouldExposeRefreshTokenTtl() {
        LoginUserDetails loginUser = loginUser();

        TokenValue refreshToken = jwtTokenService.createRefreshToken(loginUser);

        assertThat(refreshToken.expiresIn()).isEqualTo(300);
    }

    private LoginUserDetails loginUser() {
        return new LoginUserDetails(
                "u1",
                "admin",
                "管理员",
                "13800000000",
                "admin@example.com",
                null,
                List.of(new SimpleGrantedAuthority("login")));
    }

    @SpringBootApplication
    static class TestApplication {

        @RestController
        static class TestController {

            @PostMapping("/auth/login")
            R<String> login() {
                return R.success("login");
            }

            @GetMapping("/private")
            R<String> privateEndpoint() {
                return R.success("ok");
            }
        }
    }
}
