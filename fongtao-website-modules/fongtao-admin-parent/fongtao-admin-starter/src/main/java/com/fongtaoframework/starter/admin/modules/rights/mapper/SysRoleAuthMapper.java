package com.fongtaoframework.starter.admin.modules.rights.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import com.fongtaoframework.starter.mybatis.mapper.CrudMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRoleAuthMapper extends CrudMapper<SysRoleAuth> {

    @Select("""
            select distinct res.permission_code from sys_rights rights
            join sys_role role on role.sys_role_id = rights.sys_role_id and role.deleted = 0 and role.enabled = 1
            join sys_role_auth role_auth on role_auth.sys_role_id = rights.sys_role_id and role_auth.deleted = 0
            join sys_res res on res.sys_res_id = role_auth.sys_res_id and res.deleted = 0 and res.enabled = 1
            where rights.sys_user_id = #{sysUserId} and rights.defaulted = 1 and rights.enabled = 1 and rights.deleted = 0
              and res.permission_code is not null and res.permission_code <> ''
            """)
    List<String> selectDefaultPermissionsByUserId(@Param("sysUserId") String sysUserId);

    @Select("""
            select distinct res.* from sys_rights rights
            join sys_role role on role.sys_role_id = rights.sys_role_id and role.deleted = 0 and role.enabled = 1
            join sys_role_auth role_auth on role_auth.sys_role_id = rights.sys_role_id and role_auth.deleted = 0
            join sys_res res on res.sys_res_id = role_auth.sys_res_id and res.deleted = 0
            where rights.sys_user_id = #{sysUserId} and rights.defaulted = 1 and rights.enabled = 1 and rights.deleted = 0
              and res.enabled = 1 and res.visibled = 1
            order by res.sort_no asc, res.sys_res_code asc
            """)
    List<SysRes> selectDefaultVisibleResourcesByUserId(@Param("sysUserId") String sysUserId);
}
