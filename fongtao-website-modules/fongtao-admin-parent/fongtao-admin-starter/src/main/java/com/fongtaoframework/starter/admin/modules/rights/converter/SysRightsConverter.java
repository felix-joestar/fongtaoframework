package com.fongtaoframework.starter.admin.modules.rights.converter;

import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRightsRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysRightsConverter {

    @Mapping(target = "customSysOrgIds", ignore = true)
    SysRightsRow toRow(SysRights entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysRights toEntity(SysRightsCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysRights toEntity(SysRightsUpdateParam param);
}
