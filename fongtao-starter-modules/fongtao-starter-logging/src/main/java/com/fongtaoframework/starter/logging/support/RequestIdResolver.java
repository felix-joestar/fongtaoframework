package com.fongtaoframework.starter.logging.support;

import com.fongtaoframework.core.TraceIdGenerator;

public class RequestIdResolver {

    public String resolve(String candidate) {
        return TraceIdGenerator.resolve(candidate);
    }
}
