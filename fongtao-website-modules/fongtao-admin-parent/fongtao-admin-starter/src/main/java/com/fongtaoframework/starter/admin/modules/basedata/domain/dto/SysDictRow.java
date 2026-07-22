package com.fongtaoframework.starter.admin.modules.basedata.domain.dto;

public record SysDictRow(
        String sysDictId, String sysDictCode, String sysDictName, Integer enabled, String remark, Long version
) {
}
