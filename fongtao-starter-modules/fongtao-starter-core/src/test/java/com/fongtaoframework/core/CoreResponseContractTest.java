package com.fongtaoframework.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoreResponseContractTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void successResponseShouldExposeUnifiedFields() throws Exception {
        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(R.success("ok")));

        assertThat(root.path("code").asInt()).isEqualTo(ErrorCode.SUCCESS.code());
        assertThat(root.path("message").asText()).isEqualTo("操作成功");
        assertThat(root.path("data").asText()).isEqualTo("ok");
        assertThat(root.path("success").asBoolean()).isTrue();
        assertThat(root.path("traceId").asText()).isNotBlank();
        assertThat(root.path("timestamp").asText()).isNotBlank();
        assertThat(root.has("msg")).isFalse();
    }

    @Test
    void failedResponseShouldUseErrorCodeAndMessage() throws Exception {
        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(R.failed(ErrorCode.UNAUTHORIZED)));

        assertThat(root.path("code").asInt()).isEqualTo(401);
        assertThat(root.path("message").asText()).isEqualTo("未登录或登录已过期");
        assertThat(root.path("success").asBoolean()).isFalse();
        assertThat(root.path("traceId").asText()).isNotBlank();
    }

    @Test
    void pageResultShouldExposeRecordsAndPaginationMetadata() {
        PageResult<String> page = PageResult.of(List.of("a", "b"), 12, 2, 10);

        assertThat(page.records()).containsExactly("a", "b");
        assertThat(page.total()).isEqualTo(12);
        assertThat(page.pageNo()).isEqualTo(2);
        assertThat(page.pageSize()).isEqualTo(10);
        assertThat(page.pages()).isEqualTo(2);
    }

    @Test
    void pageQueryShouldNormalizePageParameters() {
        PageQuery pageQuery = PageQuery.of(0L, 1000L);

        assertThat(pageQuery.pageNo()).isEqualTo(PageQuery.DEFAULT_PAGE_NO);
        assertThat(pageQuery.pageSize()).isEqualTo(PageQuery.MAX_PAGE_SIZE);
        assertThat(pageQuery.offset()).isZero();
    }

    @Test
    void traceIdGeneratorShouldRejectUnsafeTraceId() {
        String resolved = TraceIdGenerator.resolve("bad\r\ntrace");

        assertThat(resolved).isNotEqualTo("bad\r\ntrace");
        assertThat(TraceIdGenerator.isValid(resolved)).isTrue();
    }

    @Test
    void businessExceptionShouldCarryErrorCode() {
        BusinessException exception = new BusinessException(ErrorCode.PARAM_ERROR, "参数不合法");

        assertThat(exception.code()).isEqualTo(400);
        assertThat(exception.getMessage()).isEqualTo("参数不合法");
    }
}
