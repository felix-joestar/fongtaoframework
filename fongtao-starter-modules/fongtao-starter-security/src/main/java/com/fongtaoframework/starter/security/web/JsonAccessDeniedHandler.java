package com.fongtaoframework.starter.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fongtaoframework.core.ErrorCode;
import com.fongtaoframework.core.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        JsonResponseWriter.write(response, objectMapper, HttpStatus.FORBIDDEN, R.failed(ErrorCode.FORBIDDEN));
    }
}
