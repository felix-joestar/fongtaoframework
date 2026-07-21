package com.fongtaoframework.starter.admin.modules.rights.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.fongtaoframework.starter.mybatis.mapper.CrudMapper;

@Mapper
public interface SysUserMapper extends CrudMapper<SysUser> {
}
