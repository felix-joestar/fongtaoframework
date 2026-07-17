package com.fongtaoframework.core;

import cn.hutool.core.util.StrUtil;

public final class TraceIdContext {

    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    private TraceIdContext() {
    }

    public static String currentOrCreate() {
        String traceId = TRACE_ID.get();
        if (StrUtil.isBlank(traceId)) {
            traceId = TraceIdGenerator.generate();
            TRACE_ID.set(traceId);
        }
        return traceId;
    }

    public static void set(String traceId) {
        if (StrUtil.isBlank(traceId)) {
            TRACE_ID.remove();
            return;
        }
        TRACE_ID.set(TraceIdGenerator.resolve(traceId));
    }

    public static void clear() {
        TRACE_ID.remove();
    }
}
