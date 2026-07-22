package com.fongtaoframework.starter.admin.modules.rights.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRightsMapper extends BaseMapper<SysRights> {

    @Select("""
            select * from sys_rights
            where sys_user_id = #{sysUserId} and defaulted = 1 and enabled = 1 and deleted = 0
            order by create_time asc
            """)
    List<SysRights> selectDefaultEnabledByUserId(@Param("sysUserId") String sysUserId);
}
