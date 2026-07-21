package com.fongtaoframework.starter.admin.modules;


import com.fongtaoframework.starter.admin.support.AdminMySqlIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = SysManagementControllerTest.TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SysManagementControllerTest extends AdminMySqlIntegrationTest {

    private static final String ADMIN_ROLE_ID = "20000000000000000000000000000001";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void administratorShouldAccessEveryTableObjectQueryEndpoint() throws Exception {
        String token = loginToken();

        for (String path : List.of(
                "/sys-user/page", "/sys-org/page", "/sys-org/tree", "/sys-role/page", "/sys-role/tree",
                "/sys-res/page", "/sys-res/tree", "/sys-rights/page", "/sys-dict/page", "/sys-dict-item/page",
                "/sys-config/page", "/sys-serial/page")) {
            postAs(token, path, "{}").andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));
        }

        postAs(token, "/sys-role-auth/list-auth-res", "{\"sysRoleId\":\"" + ADMIN_ROLE_ID + "\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
        postAs(token, "/sys-dict/options", "{\"sysDictCode\":\"missing-dictionary\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void oldAggregateManagementPathsShouldNotBeExposed() throws Exception {
        String token = loginToken();
        postAs(token, "/admin/rights/users", "{}").andExpect(status().isNotFound());
        postAs(token, "/admin/basedata/dictionaries", "{}").andExpect(status().isNotFound());
    }

    @Test
    void administratorShouldReachEveryTableObjectMutationEndpoint() throws Exception {
        String token = loginToken();

        for (String path : List.of(
                "/sys-user/get-by-id", "/sys-user/create", "/sys-user/update-by-id", "/sys-user/delete-by-id",
                "/sys-user/update-status", "/sys-user/update-password", "/sys-org/get-by-id", "/sys-org/create",
                "/sys-org/update-by-id", "/sys-org/delete-by-id", "/sys-role/get-by-id", "/sys-role/create",
                "/sys-role/update-by-id", "/sys-role/delete-by-id", "/sys-res/get-by-id", "/sys-res/create",
                "/sys-res/update-by-id", "/sys-res/delete-by-id", "/sys-role-auth/auth-res", "/sys-rights/get-by-id",
                "/sys-rights/create", "/sys-rights/update-by-id", "/sys-rights/update-enabled", "/sys-rights/delete-by-id",
                "/sys-dict/get-by-id", "/sys-dict/create", "/sys-dict/update-by-id", "/sys-dict/delete-by-id",
                "/sys-dict-item/get-by-id", "/sys-dict-item/create", "/sys-dict-item/update-by-id", "/sys-dict-item/delete-by-id",
                "/sys-config/get-by-id", "/sys-config/create", "/sys-config/update-by-id", "/sys-config/delete-by-id",
                "/sys-serial/get-by-id", "/sys-serial/create", "/sys-serial/update-by-id", "/sys-serial/delete-by-id",
                "/sys-serial/next")) {
            postAs(token, path, "{}").andExpect(status().isBadRequest());
        }

        postAs(token, "/sys-role-auth/list-auth-res", "{}").andExpect(status().isBadRequest());
        mockMvc.perform(post("/sys-dict/options")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isBadRequest());
    }

    private String loginToken() throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken", not(blankOrNullString())))
                .andReturn().getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(response);
        return root.path("data").path("accessToken").asText();
    }

    private org.springframework.test.web.servlet.ResultActions postAs(String token, String path, String body)
            throws Exception {
        return mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(body)
                .header(HttpHeaders.AUTHORIZATION, bearer(token)));
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    @SpringBootApplication
    static class TestApplication {
    }
}
