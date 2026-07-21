package com.fongtaoframework.starter.admin.modules.rights.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRightsExtra;
import com.fongtaoframework.starter.mybatis.mapper.CrudMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRightsExtraMapper extends CrudMapper<SysRightsExtra> {

    @Select("""
            select sys_org_id from sys_rights_extra
            where sys_rights_id = #{sysRightsId} and deleted = 0
            order by create_time asc
            """)
    List<String> selectOrgIdsByRightsId(@Param("sysRightsId") String sysRightsId);
}
