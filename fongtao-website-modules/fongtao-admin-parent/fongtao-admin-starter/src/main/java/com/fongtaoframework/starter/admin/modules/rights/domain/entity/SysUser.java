package com.fongtaoframework.starter.admin.modules.rights.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_user")
public class SysUser extends CrudEntity {

    @TableId("sys_user_id")
    private String sysUserId;
    private String sysUserCode;
    private String sysUserPwd;
    private String sysUserName;
    private String sysUserMobile;
    private String sysUserEmail;
    private String avatarFileId;
    private Integer sysUserStatus;
    private String sysData;
}
