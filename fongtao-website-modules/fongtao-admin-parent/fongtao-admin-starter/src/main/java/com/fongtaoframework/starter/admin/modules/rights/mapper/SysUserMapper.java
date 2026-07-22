package com.fongtaoframework.starter.admin.modules.rights.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
