package com.fongtaoframework.starter.admin.modules.rights.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import com.fongtaoframework.starter.mybatis.mapper.CrudMapper;

@Mapper
public interface SysRoleMapper extends CrudMapper<SysRole> {
}
