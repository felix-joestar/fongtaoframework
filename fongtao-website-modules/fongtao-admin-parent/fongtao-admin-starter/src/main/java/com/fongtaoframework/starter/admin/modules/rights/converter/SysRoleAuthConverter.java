package com.fongtaoframework.starter.admin.modules.rights.converter;

import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleAuthCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysRoleAuthConverter {

    @Mapping(target = "sysRoleAuthId", ignore = true)
    SysRoleAuth toEntity(SysRoleAuthCreateParam param);
}
