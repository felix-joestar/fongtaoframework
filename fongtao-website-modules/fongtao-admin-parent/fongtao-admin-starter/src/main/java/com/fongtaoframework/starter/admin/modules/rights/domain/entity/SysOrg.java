package com.fongtaoframework.starter.admin.modules.rights.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_org")
public class SysOrg extends CrudEntity {

    @TableId(value = "sys_org_id", type = IdType.INPUT)
    private String sysOrgId;
    private String parentId;
    private String sysOrgCode;
    private String sysOrgName;
    private String sysOrgType;
    private Integer enabled;
    private Integer sortNo;
    private String remark;
}
