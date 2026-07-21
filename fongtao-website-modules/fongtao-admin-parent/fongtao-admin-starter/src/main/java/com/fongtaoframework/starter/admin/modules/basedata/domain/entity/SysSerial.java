package com.fongtaoframework.starter.admin.modules.basedata.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_serial")
public class SysSerial extends CrudEntity {

    @TableId(value = "sys_serial_id", type = IdType.INPUT)
    private String sysSerialId;
    private String serialCode;
    private String serialName;
    private String serialPrefix;
    private String datePattern;
    private Integer serialLength;
    private Long currentValue;
    private Integer stepValue;
    private String cycleType;
    private String lastCycle;
    private Integer enabled;
    private String remark;
}
