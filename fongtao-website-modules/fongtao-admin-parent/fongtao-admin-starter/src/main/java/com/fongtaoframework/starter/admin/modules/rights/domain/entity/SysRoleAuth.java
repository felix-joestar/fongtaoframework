package com.fongtaoframework.starter.admin.modules.rights.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_role_auth")
public class SysRoleAuth extends CrudEntity {

    @TableId(value = "sys_role_auth_id", type = IdType.INPUT)
    private String sysRoleAuthId;
    private String sysRoleId;
    private String sysResId;
}
