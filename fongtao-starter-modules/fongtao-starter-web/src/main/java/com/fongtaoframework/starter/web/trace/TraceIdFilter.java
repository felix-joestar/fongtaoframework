package com.fongtaoframework.starter.web.trace;

import cn.hutool.core.util.StrUtil;
import com.fongtaoframework.core.TraceIdContext;
import com.fongtaoframework.starter.logging.support.RequestIdResolver;
import com.fongtaoframework.starter.web.properties.WebStarterProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

    private final WebStarterProperties.Trace properties;
    private final RequestIdResolver requestIdResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(properties.getHeaderName());
        if (StrUtil.isBlank(traceId)) {
            traceId = TraceIdContext.currentOrCreate();
        }
        traceId = requestIdResolver.resolve(traceId);
        TraceIdContext.set(traceId);
        MDC.put(properties.getMdcName(), traceId);
        response.setHeader(properties.getHeaderName(), traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            response.setHeader(properties.getHeaderName(), traceId);
            MDC.remove(properties.getMdcName());
            TraceIdContext.clear();
        }
    }
}
