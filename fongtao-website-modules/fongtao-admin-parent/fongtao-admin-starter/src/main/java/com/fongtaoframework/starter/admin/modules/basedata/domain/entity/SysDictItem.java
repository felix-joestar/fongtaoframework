package com.fongtaoframework.starter.admin.modules.basedata.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fongtaoframework.starter.mybatis.domain.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_dict_item")
public class SysDictItem extends CrudEntity {

    @TableId("sys_dict_item_id")
    private String sysDictItemId;
    private String sysDictId;
    private String dictItemValue;
    private String dictItemLabel;
    private Integer enabled;
    private Integer sortNo;
    private String remark;
}
