package com.fongtaoframework.starter.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fongtaoframework.starter.core.result.CommonErrorCode;
import com.fongtaoframework.starter.core.result.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@RequiredArgsConstructor
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        JsonResponseWriter.write(response, objectMapper, HttpStatus.UNAUTHORIZED, R.failed(CommonErrorCode.UNAUTHORIZED));
    }
}
