package com.fongtaoframework.starter.admin.modules.rights.converter;

import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysResConverter {

    @Mapping(target = "children", ignore = true)
    SysResRow toRow(SysRes entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysRes toEntity(SysResCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysRes toEntity(SysResUpdateParam param);
}
