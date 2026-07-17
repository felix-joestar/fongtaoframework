package com.fongtaoframework.starter.mybatis.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CrudEntity extends AuditEntity {

    @Version
    @TableField("version")
    private Long version;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
