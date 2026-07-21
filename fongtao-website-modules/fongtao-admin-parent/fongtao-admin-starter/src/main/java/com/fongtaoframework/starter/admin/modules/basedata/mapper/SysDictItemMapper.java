package com.fongtaoframework.starter.admin.modules.basedata.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.mybatis.mapper.CrudMapper;

@Mapper
public interface SysDictItemMapper extends CrudMapper<SysDictItem> {
}
