package com.fongtaoframework.starter.admin.modules.rights.converter;

import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRoleRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", implementationName = "<CLASS_NAME>Generated", implementationPackage = "<PACKAGE_NAME>.impl", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysRoleConverter {

    @Mapping(target = "children", ignore = true)
    SysRoleRow toRow(SysRole entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysRole toEntity(SysRoleCreateParam param);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SysRole toEntity(SysRoleUpdateParam param);
}
