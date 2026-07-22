package com.fongtaoframework.starter.logging.web;

import com.fongtaoframework.starter.logging.properties.LoggingStarterProperties;
import com.fongtaoframework.starter.core.trace.TraceIdGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final LoggingStarterProperties.Request properties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String traceId = TraceIdGenerator.resolve(request.getHeader(properties.getTraceHeaderName()));
        try {
            filterChain.doFilter(request, response);
        } finally {
            String responseTraceId = response.getHeader(properties.getTraceHeaderName());
            if (responseTraceId != null) {
                traceId = TraceIdGenerator.resolve(responseTraceId);
            }
            log.info(
                    "http_request method={} uri={} status={} durationMs={} traceId={} clientIp={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    System.currentTimeMillis() - start,
                    traceId,
                    request.getRemoteAddr());
        }
    }
}
