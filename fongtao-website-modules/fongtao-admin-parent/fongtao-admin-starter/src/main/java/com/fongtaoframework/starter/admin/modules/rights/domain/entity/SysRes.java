package com.fongtaoframework.starter.admin.modules.rights.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_res")
public class SysRes extends CrudEntity {

    @TableId("sys_res_id")
    private String sysResId;
    private String parentId;
    private String sysResCode;
    private String sysResName;
    private String sysResType;
    private String permissionCode;
    private String routePath;
    private String componentPath;
    private String icon;
    private Integer visibled;
    private Integer enabled;
    private Integer sortNo;
    private String sysData;
    private String remark;
}
