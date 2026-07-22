package com.fongtaoframework.starter.admin.modules.rights.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_rights_extra")
public class SysRightsExtra extends CrudEntity {

    @TableId("sys_rights_extra_id")
    private String sysRightsExtraId;
    private String sysRightsId;
    private String sysOrgId;
}
