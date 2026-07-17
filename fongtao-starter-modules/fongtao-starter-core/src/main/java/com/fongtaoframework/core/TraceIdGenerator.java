package com.fongtaoframework.core;

import cn.hutool.core.util.StrUtil;
import java.util.UUID;
import java.util.regex.Pattern;

public final class TraceIdGenerator {

    private static final int MAX_TRACE_ID_LENGTH = 100;
    private static final Pattern SAFE_TRACE_ID = Pattern.compile("[A-Za-z0-9._:-]{1,100}");

    private TraceIdGenerator() {
    }

    public static String resolve(String candidate) {
        if (isValid(candidate)) {
            return candidate;
        }
        return generate();
    }

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isValid(String traceId) {
        return StrUtil.isNotBlank(traceId)
                && traceId.length() <= MAX_TRACE_ID_LENGTH
                && SAFE_TRACE_ID.matcher(traceId).matches();
    }
}
