package com.fongtaoframework.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_user")
public class SysUser {

    @TableId(value = "sys_user_id", type = IdType.INPUT)
    private String sysUserId;
    private String sysUserCode;
    private String sysUserPwd;
    private String sysUserName;
    private String sysUserMobile;
    private String sysUserEmail;
    private String avatarFileId;
    private Integer sysUserStatus;
    private String sysData;
    private String createId;
    private LocalDateTime createTime;
    private String updateId;
    private LocalDateTime updateTime;
    private Integer version;
    private Integer deleted;
}
