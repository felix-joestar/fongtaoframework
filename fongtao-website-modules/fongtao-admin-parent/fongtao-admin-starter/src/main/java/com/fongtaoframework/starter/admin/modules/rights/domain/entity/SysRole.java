package com.fongtaoframework.starter.admin.modules.rights.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_role")
public class SysRole extends CrudEntity {

    @TableId("sys_role_id")
    private String sysRoleId;
    private String parentId;
    private String sysRoleCode;
    private String sysRoleName;
    private Integer enabled;
    private Integer sortNo;
    private String remark;
}
