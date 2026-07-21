package com.fongtaoframework.starter.admin.modules.rights.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_rights")
public class SysRights extends CrudEntity {

    @TableId(value = "sys_rights_id", type = IdType.INPUT)
    private String sysRightsId;
    private String sysUserId;
    private String sysOrgId;
    private String sysRoleId;
    private Integer defaulted;
    private Integer enabled;
    private String dataScope;
    private String remark;
}
