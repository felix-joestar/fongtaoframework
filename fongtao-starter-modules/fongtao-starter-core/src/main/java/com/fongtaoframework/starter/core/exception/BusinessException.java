package com.fongtaoframework.starter.core.exception;

import com.fongtaoframework.starter.core.result.CommonErrorCode;
import com.fongtaoframework.starter.core.result.ErrorCode;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(String message) {
        this(CommonErrorCode.BUSINESS_ERROR, message);
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.message());
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable cause) {
        this(CommonErrorCode.BUSINESS_ERROR, message, cause);
    }

    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }

    public int code() {
        return errorCode.code();
    }
}
