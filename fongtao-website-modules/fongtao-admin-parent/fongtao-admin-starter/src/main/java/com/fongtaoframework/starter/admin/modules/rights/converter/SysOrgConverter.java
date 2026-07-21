package com.fongtaoframework.starter.admin.modules.rights.converter;

import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysOrgRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.BeanMapping;

@Mapper(
        componentModel = "spring",
        implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysOrgConverter {

    @Mapping(target = "children", ignore = true)
    SysOrgRow toRow(SysOrg entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysOrg toEntity(SysOrgCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysOrg toEntity(SysOrgUpdateParam param);
}
