package com.fongtaoframework.core;

import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;

@Getter
public class R<T> implements Serializable {

    private final int code;
    private final String message;
    private final T data;
    private final boolean success;
    private final String traceId;
    private final String timestamp;

    private R(int code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.traceId = TraceIdContext.currentOrCreate();
        this.timestamp = Instant.now().toString();
    }

    public static <T> R<T> success() {
        return success(null);
    }

    public static <T> R<T> success(T data) {
        return new R<>(ErrorCode.SUCCESS.code(), ErrorCode.SUCCESS.message(), data, true);
    }

    public static <T> R<T> failed(String message) {
        return failed(ErrorCode.INTERNAL_ERROR.code(), message);
    }

    public static <T> R<T> failed(ErrorCode errorCode) {
        return failed(errorCode.code(), errorCode.message());
    }

    public static <T> R<T> failed(int code, String message) {
        return new R<>(code, message, null, false);
    }
}
