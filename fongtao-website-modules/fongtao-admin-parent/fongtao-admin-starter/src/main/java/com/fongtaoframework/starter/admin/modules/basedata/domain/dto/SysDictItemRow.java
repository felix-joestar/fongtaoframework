package com.fongtaoframework.starter.admin.modules.basedata.domain.dto;

public record SysDictItemRow(
        String sysDictItemId, String sysDictId, String dictItemValue, String dictItemLabel,
        Integer enabled, Integer sortNo, String remark
) {
}
