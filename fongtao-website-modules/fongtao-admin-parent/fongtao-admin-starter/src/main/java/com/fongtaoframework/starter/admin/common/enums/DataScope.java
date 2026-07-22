package com.fongtaoframework.starter.admin.common.enums;

import java.util.Arrays;

public enum DataScope {
    SELF("self"),
    SYS_ORG("sys-org"),
    SYS_ORG_AND_CHILDREN("sys-org-and-children"),
    ALL("all"),
    CUSTOM("custom");

    private final String code;

    DataScope(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean supports(String code) {
        return Arrays.stream(values()).anyMatch(scope -> scope.code.equals(code));
    }
}
