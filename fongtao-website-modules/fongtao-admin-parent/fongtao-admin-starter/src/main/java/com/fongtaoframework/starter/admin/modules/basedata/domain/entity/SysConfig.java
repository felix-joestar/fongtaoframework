package com.fongtaoframework.starter.admin.modules.basedata.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_config")
public class SysConfig extends CrudEntity {

    @TableId("sys_config_id")
    private String sysConfigId;
    private String sysConfigCode;
    private String sysConfigName;
    private String sysConfigValue;
    private String valueType;
    private Integer editable;
    private Integer enabled;
    private String remark;
}
