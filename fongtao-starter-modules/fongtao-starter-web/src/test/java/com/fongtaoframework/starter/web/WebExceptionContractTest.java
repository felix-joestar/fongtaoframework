package com.fongtaoframework.starter.web;

import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.ErrorCode;
import com.fongtaoframework.core.R;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = WebExceptionContractTest.TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class WebExceptionContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void businessExceptionShouldReturnUnifiedResponseWithTraceId() throws Exception {
        mockMvc.perform(get("/business").header("X-Trace-Id", "trace-001"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Trace-Id", "trace-001"))
                .andExpect(jsonPath("$.code").value(ErrorCode.BUSINESS_ERROR.code()))
                .andExpect(jsonPath("$.message").value("业务失败"))
                .andExpect(jsonPath("$.traceId").value("trace-001"));
    }

    @Test
    void validationExceptionShouldReturnUnifiedParamError() throws Exception {
        mockMvc.perform(get("/validation").param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Trace-Id", not(blankOrNullString())))
                .andExpect(jsonPath("$.code").value(ErrorCode.PARAM_ERROR.code()))
                .andExpect(jsonPath("$.message", not(blankOrNullString())))
                .andExpect(jsonPath("$.traceId", not(blankOrNullString())));
    }

    @Test
    void unsafeTraceHeaderShouldBeReplaced() throws Exception {
        mockMvc.perform(get("/validation").param("name", "").header("X-Trace-Id", "bad trace"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Trace-Id", not("bad trace")))
                .andExpect(jsonPath("$.traceId", not("bad trace")));
    }

    @Test
    void unknownExceptionShouldHideInternalMessage() throws Exception {
        mockMvc.perform(get("/unknown"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL_ERROR.code()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INTERNAL_ERROR.message()))
                .andExpect(jsonPath("$.traceId", not(blankOrNullString())));
    }

    @SpringBootApplication
    static class TestApplication {

        @Validated
        @RestController
        static class TestController {

            @GetMapping("/business")
            R<Void> business() {
                throw new BusinessException("业务失败");
            }

            @GetMapping("/validation")
            R<String> validation(@RequestParam @NotBlank(message = "名称不能为空") String name) {
                return R.success(name);
            }

            @GetMapping("/unknown")
            R<Void> unknown() {
                throw new IllegalStateException("数据库密码泄露风险文本");
            }
        }
    }
}
