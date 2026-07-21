package com.fongtaoframework.starter.admin.modules.basedata.converter;

import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysConfigRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysConfigUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysConfigConverter {

    SysConfigRow toRow(SysConfig entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysConfig toEntity(SysConfigCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysConfig toEntity(SysConfigUpdateParam param);
}
