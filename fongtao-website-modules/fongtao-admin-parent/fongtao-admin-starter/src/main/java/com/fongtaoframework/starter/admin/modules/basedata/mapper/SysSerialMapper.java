package com.fongtaoframework.starter.admin.modules.basedata.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysSerialMapper extends BaseMapper<SysSerial> {

    @Select("select * from sys_serial where serial_code = #{serialCode} and deleted = 0 for update")
    SysSerial selectByCodeForUpdate(@Param("serialCode") String serialCode);
}
