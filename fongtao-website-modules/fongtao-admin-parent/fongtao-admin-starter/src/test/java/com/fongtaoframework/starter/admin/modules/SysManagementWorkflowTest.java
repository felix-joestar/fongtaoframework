package com.fongtaoframework.starter.admin.modules;


import com.fongtaoframework.starter.admin.support.AdminMySqlIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = SysManagementWorkflowTest.TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SysManagementWorkflowTest extends AdminMySqlIntegrationTest {

    private static final String ROOT_ORG_ID = "10000000000000000000000000000001";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void administratorShouldCreateEveryTableObject() throws Exception {
        String token = loginToken();

        postSuccess(token, "/sys-user/create", Map.of(
                "sysUserCode", "workflow-user", "password", "workflow-password",
                "sysUserName", "流程用户", "sysUserStatus", 1));
        String userId = idByCode("sys_user", "sys_user_id", "sys_user_code", "workflow-user");

        postSuccess(token, "/sys-org/create", Map.of(
                "parentId", ROOT_ORG_ID, "sysOrgCode", "workflow-org", "sysOrgName", "流程组织",
                "sysOrgType", "department", "enabled", 1, "sortNo", 100));
        String orgId = idByCode("sys_org", "sys_org_id", "sys_org_code", "workflow-org");

        postSuccess(token, "/sys-role/create", Map.of(
                "sysRoleCode", "workflow-role", "sysRoleName", "流程角色", "enabled", 1, "sortNo", 100));
        String roleId = idByCode("sys_role", "sys_role_id", "sys_role_code", "workflow-role");

        postSuccess(token, "/sys-res/create", Map.of(
                "sysResCode", "workflow-resource", "sysResName", "流程资源", "sysResType", "api",
                "permissionCode", "workflow:read", "visibled", 1, "enabled", 1, "sortNo", 100));
        String resId = idByCode("sys_res", "sys_res_id", "sys_res_code", "workflow-resource");

        postSuccess(token, "/sys-role-auth/auth-res", Map.of("sysRoleId", roleId, "sysResIds", List.of(resId)));
        postAs(token, "/sys-role-auth/list-auth-res", Map.of("sysRoleId", roleId))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data[0].sysResId").value(resId));

        postSuccess(token, "/sys-rights/create", Map.of(
                "sysUserId", userId, "sysOrgId", orgId, "sysRoleId", roleId,
                "defaulted", 1, "enabled", 1, "dataScope", "custom", "customSysOrgIds", List.of(orgId)));
        String rightsId = jdbcTemplate.queryForObject(
                "select sys_rights_id from sys_rights where sys_user_id = ? and deleted = 0", String.class, userId);

        postSuccess(token, "/sys-dict/create", Map.of(
                "sysDictCode", "workflow-dict", "sysDictName", "流程字典", "enabled", 1));
        String dictId = idByCode("sys_dict", "sys_dict_id", "sys_dict_code", "workflow-dict");
        postSuccess(token, "/sys-dict-item/create", Map.of(
                "sysDictId", dictId, "dictItemValue", "workflow-value", "dictItemLabel", "流程字典项",
                "enabled", 1, "sortNo", 100));

        postSuccess(token, "/sys-config/create", Map.of(
                "sysConfigCode", "workflow.config", "sysConfigName", "流程配置", "sysConfigValue", "workflow",
                "valueType", "string", "editable", 1, "enabled", 1));

        postSuccess(token, "/sys-serial/create", Map.of(
                "serialCode", "workflow-serial", "serialName", "流程流水号", "serialPrefix", "WF-",
                "serialLength", 4, "currentValue", 0, "stepValue", 1, "cycleType", "none", "enabled", 1));
        postAs(token, "/sys-serial/next", Map.of("serialCode", "workflow-serial"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data").value("WF-0001"));

        postAs(token, "/sys-dict/options", Map.of("sysDictCode", "workflow-dict"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data[0].dictItemValue").value("workflow-value"));
        assertEquals(1, jdbcTemplate.queryForObject(
                "select count(*) from sys_rights_extra where sys_rights_id = ? and sys_org_id = ? and deleted = 0",
                Integer.class, rightsId, orgId));
    }

    @Test
    void administratorShouldEnforceTreeReferenceIdentityAndSerialRules() throws Exception {
        String token = loginToken();

        postSuccess(token, "/sys-org/create", Map.of(
                "parentId", ROOT_ORG_ID, "sysOrgCode", "guard-org", "sysOrgName", "校验组织",
                "sysOrgType", "department", "enabled", 1, "sortNo", 101));
        String orgId = idByCode("sys_org", "sys_org_id", "sys_org_code", "guard-org");
        postFailure(token, "/sys-org/update-by-id", Map.of(
                "sysOrgId", ROOT_ORG_ID, "parentId", orgId, "sysOrgCode", "root", "sysOrgName", "默认组织",
                "sysOrgType", "sys-org", "enabled", 1, "sortNo", 0));
        postFailure(token, "/sys-org/delete-by-id", Map.of("sysOrgId", ROOT_ORG_ID));

        postSuccess(token, "/sys-role/create", Map.of(
                "sysRoleCode", "guard-role-one", "sysRoleName", "校验角色一", "enabled", 1, "sortNo", 101));
        String roleOneId = idByCode("sys_role", "sys_role_id", "sys_role_code", "guard-role-one");
        postFailure(token, "/sys-role/update-by-id", Map.of(
                "sysRoleId", roleOneId, "parentId", roleOneId, "sysRoleCode", "guard-role-one",
                "sysRoleName", "校验角色一", "enabled", 1, "sortNo", 101));

        postSuccess(token, "/sys-role/create", Map.of(
                "sysRoleCode", "guard-role-two", "sysRoleName", "校验角色二", "enabled", 1, "sortNo", 102));
        String roleTwoId = idByCode("sys_role", "sys_role_id", "sys_role_code", "guard-role-two");
        postSuccess(token, "/sys-user/create", Map.of(
                "sysUserCode", "guard-user", "password", "guard-password", "sysUserName", "校验用户",
                "sysUserStatus", 1));
        String userId = idByCode("sys_user", "sys_user_id", "sys_user_code", "guard-user");

        postSuccess(token, "/sys-rights/create", Map.of(
                "sysUserId", userId, "sysOrgId", ROOT_ORG_ID, "sysRoleId", roleOneId,
                "defaulted", 1, "enabled", 1, "dataScope", "all", "customSysOrgIds", List.of()));
        postSuccess(token, "/sys-rights/create", Map.of(
                "sysUserId", userId, "sysOrgId", ROOT_ORG_ID, "sysRoleId", roleTwoId,
                "defaulted", 1, "enabled", 1, "dataScope", "all", "customSysOrgIds", List.of()));
        assertEquals(1, jdbcTemplate.queryForObject(
                "select count(*) from sys_rights where sys_user_id = ? and defaulted = 1 and deleted = 0",
                Integer.class, userId));
        postFailure(token, "/sys-user/delete-by-id", Map.of("sysUserId", userId));

        postSuccess(token, "/sys-serial/create", Map.of(
                "serialCode", "guard-serial", "serialName", "校验流水号", "serialPrefix", "G-",
                "serialLength", 2, "currentValue", 0, "stepValue", 1, "cycleType", "none", "enabled", 1));
        postAs(token, "/sys-serial/next", Map.of("serialCode", "guard-serial"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data").value("G-01"));
        postAs(token, "/sys-serial/next", Map.of("serialCode", "guard-serial"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data").value("G-02"));
    }

    private String loginToken() throws Exception {
        String response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"password\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.accessToken", not(blankOrNullString())))
                .andReturn().getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(response);
        return root.path("data").path("accessToken").asText();
    }

    private void postSuccess(String token, String path, Object body) throws Exception {
        postAs(token, path, body).andExpect(status().isOk()).andExpect(jsonPath("$.success", is(true)));
    }

    private void postFailure(String token, String path, Object body) throws Exception {
        postAs(token, path, body).andExpect(status().isBadRequest()).andExpect(jsonPath("$.success", is(false)));
    }

    private ResultActions postAs(String token, String path, Object body) throws Exception {
        return mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
    }

    private String idByCode(String table, String idColumn, String codeColumn, String code) {
        return jdbcTemplate.queryForObject(
                "select " + idColumn + " from " + table + " where " + codeColumn + " = ? and deleted = 0",
                String.class, code);
    }

    @SpringBootApplication
    static class TestApplication {
    }
}
