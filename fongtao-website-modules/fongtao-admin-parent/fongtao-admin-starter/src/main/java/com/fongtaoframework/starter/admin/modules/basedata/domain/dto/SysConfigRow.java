package com.fongtaoframework.starter.admin.modules.basedata.domain.dto;

public record SysConfigRow(
        String sysConfigId, String sysConfigCode, String sysConfigName, String sysConfigValue,
        String valueType, Integer editable, Integer enabled, String remark, Long version
) {
}
