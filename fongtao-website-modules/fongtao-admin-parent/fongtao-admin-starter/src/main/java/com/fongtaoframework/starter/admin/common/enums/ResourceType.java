package com.fongtaoframework.starter.admin.common.enums;

import java.util.Arrays;

public enum ResourceType {
    MENU("menu"),
    BUTTON("button"),
    API("api");

    private final String code;

    ResourceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean supports(String code) {
        return Arrays.stream(values()).anyMatch(type -> type.code.equals(code));
    }
}
