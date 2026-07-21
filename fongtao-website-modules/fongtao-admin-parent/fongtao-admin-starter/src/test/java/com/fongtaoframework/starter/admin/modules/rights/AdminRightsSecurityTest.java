package com.fongtaoframework.starter.admin.modules.rights;


import com.fongtaoframework.starter.admin.support.AdminMySqlIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = AdminRightsSecurityTest.TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AdminRightsSecurityTest extends AdminMySqlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void administratorShouldLoadPermissionsAndAccessProtectedUserPage() throws Exception {
        String token = loginToken("admin", "password");

        mockMvc.perform(get("/auth/login-user").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.permissions", hasItem("admin:sys-user:page")))
                .andExpect(jsonPath("$.data.defaultIdentity.rightsId").value("50000000000000000000000000000001"))
                .andExpect(jsonPath("$.data.defaultIdentity.dataScope").value("all"));

        mockMvc.perform(get("/auth/login-user/resources").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].sysResCode").value("admin"));

        mockMvc.perform(post("/sys-user/page").contentType(MediaType.APPLICATION_JSON).content("{}")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    void disabledUserShouldNotAccessAdminManagement() throws Exception {
        String token = loginToken("operator", "password");

        mockMvc.perform(post("/sys-user/page").contentType(MediaType.APPLICATION_JSON).content("{}")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    void disabledRoleOrResourceShouldNotGrantPermissionsAndVisibleResourcesShouldBeTree() throws Exception {
        String pageResourceId = "30000000000000000000000000000002";
        try {
            jdbcTemplate.update("update sys_res set visibled = 1 where sys_res_id = ?", pageResourceId);
            String initialToken = loginToken("admin", "password");
            mockMvc.perform(get("/auth/login-user/resources").header(HttpHeaders.AUTHORIZATION, "Bearer " + initialToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].sysResCode").value("admin"))
                    .andExpect(jsonPath("$.data[0].children[0].sysResCode").value("admin-sys-user-page"));

            jdbcTemplate.update("update sys_res set enabled = 0 where sys_res_id = ?", pageResourceId);
            String resourceDisabledToken = loginToken("admin", "password");
            mockMvc.perform(get("/auth/login-user").header(HttpHeaders.AUTHORIZATION, "Bearer " + resourceDisabledToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.permissions", not(hasItem("admin:sys-user:page"))));
            mockMvc.perform(post("/sys-user/page").contentType(MediaType.APPLICATION_JSON).content("{}")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + resourceDisabledToken))
                    .andExpect(status().isForbidden());

            jdbcTemplate.update("update sys_res set enabled = 1 where sys_res_id = ?", pageResourceId);
            jdbcTemplate.update("update sys_role set enabled = 0 where sys_role_code = ?", "administrator");
            String roleDisabledToken = loginToken("admin", "password");
            mockMvc.perform(get("/auth/login-user").header(HttpHeaders.AUTHORIZATION, "Bearer " + roleDisabledToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.permissions").isEmpty());
            mockMvc.perform(get("/auth/login-user/resources").header(HttpHeaders.AUTHORIZATION, "Bearer " + roleDisabledToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isEmpty());
            mockMvc.perform(post("/sys-user/page").contentType(MediaType.APPLICATION_JSON).content("{}")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + roleDisabledToken))
                    .andExpect(status().isForbidden());
        } finally {
            jdbcTemplate.update("update sys_res set enabled = 1, visibled = 0 where sys_res_id = ?", pageResourceId);
            jdbcTemplate.update("update sys_role set enabled = 1 where sys_role_code = ?", "administrator");
        }
    }

    private String loginToken(String username, String password) throws Exception {
        String json = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken", not(blankOrNullString())))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode root = objectMapper.readTree(json);
        return root.path("data").path("accessToken").asText();
    }

    @SpringBootApplication
    static class TestApplication {
    }
}
