package com.fongtaoframework.starter.core;

import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.core.result.CommonErrorCode;
import com.fongtaoframework.starter.core.result.ErrorCode;
import com.fongtaoframework.starter.core.result.R;
import com.fongtaoframework.starter.core.trace.TraceIdGenerator;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoreResponseContractTest {

    @Test
    void successResponseShouldExposeUnifiedFields() {
        R<String> response = R.success("ok");

        assertThat(response.getCode()).isEqualTo(CommonErrorCode.SUCCESS.code());
        assertThat(response.getMessage()).isEqualTo("操作成功");
        assertThat(response.getData()).isEqualTo("ok");
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getTraceId()).isNotBlank();
        assertThat(response.getTimestamp()).isNotBlank();
    }

    @Test
    void failedResponseShouldUseErrorCodeAndMessage() {
        R<Object> response = R.failed(CommonErrorCode.UNAUTHORIZED);

        assertThat(response.getCode()).isEqualTo(401);
        assertThat(response.getMessage()).isEqualTo("未登录或登录已过期");
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getTraceId()).isNotBlank();
    }

    @Test
    void pageResultShouldExposeRecordsAndPaginationMetadata() {
        PageResult<String> page = PageResult.of(List.of("a", "b"), 12, 2, 10);

        assertThat(page.records()).containsExactly("a", "b");
        assertThat(page.total()).isEqualTo(12);
        assertThat(page.pageNo()).isEqualTo(2);
        assertThat(page.pageSize()).isEqualTo(10);
        assertThat(page.pages()).isEqualTo(2);
        assertThat(page.map(String::length).records()).containsExactly(1, 1);
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
        RuntimeException cause = new RuntimeException("root cause");
        BusinessException exception = new BusinessException(TestErrorCode.CONFLICT, "参数不合法", cause);

        assertThat(exception.code()).isEqualTo(20001);
        assertThat(exception.getMessage()).isEqualTo("参数不合法");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    private enum TestErrorCode implements ErrorCode {
        CONFLICT(20001, "状态冲突");

        private final int code;
        private final String message;

        TestErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public int code() {
            return code;
        }

        @Override
        public String message() {
            return message;
        }
    }
}
