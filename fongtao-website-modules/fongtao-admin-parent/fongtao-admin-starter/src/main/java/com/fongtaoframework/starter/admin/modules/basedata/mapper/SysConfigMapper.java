package com.fongtaoframework.starter.admin.modules.basedata.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;
import com.fongtaoframework.starter.mybatis.mapper.CrudMapper;

@Mapper
public interface SysConfigMapper extends CrudMapper<SysConfig> {
}
