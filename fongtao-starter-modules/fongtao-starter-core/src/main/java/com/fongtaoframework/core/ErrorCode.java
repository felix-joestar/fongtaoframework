package com.fongtaoframework.core;

public enum ErrorCode {

    SUCCESS(0, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    INTERNAL_ERROR(500, "系统异常"),
    BUSINESS_ERROR(10000, "业务处理失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
