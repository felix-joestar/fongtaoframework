package com.fongtaoframework.starter.admin.modules.basedata.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {
}
