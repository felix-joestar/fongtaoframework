package com.fongtaoframework.starter.admin.modules.basedata.domain.dto;

public record SysSerialRow(
        String sysSerialId, String serialCode, String serialName, String serialPrefix, String datePattern,
        Integer serialLength, Long currentValue, Integer stepValue, String cycleType, String lastCycle, Integer enabled, String remark
) {
}
