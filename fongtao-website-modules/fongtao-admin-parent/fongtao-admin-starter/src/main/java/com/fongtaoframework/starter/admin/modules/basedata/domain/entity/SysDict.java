package com.fongtaoframework.starter.admin.modules.basedata.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_dict")
public class SysDict extends CrudEntity {

    @TableId("sys_dict_id")
    private String sysDictId;
    private String sysDictCode;
    private String sysDictName;
    private Integer enabled;
    private String remark;
}
