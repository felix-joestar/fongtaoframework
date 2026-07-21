package com.fongtaoframework.starter.admin.modules.basedata.converter;

import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysSerialRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysSerialUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysSerial;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysSerialConverter {

    SysSerialRow toRow(SysSerial entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysSerial toEntity(SysSerialCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysSerial toEntity(SysSerialUpdateParam param);
}
