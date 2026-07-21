package com.fongtaoframework.starter.admin.modules.rights.converter;

import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysUserRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserConverter {

    @Mapping(target = "sysUserPwd", ignore = true)
    SysUser toEntity(SysUserCreateParam param);

    @Mapping(target = "sysUserPwd", ignore = true)
    SysUser toEntity(SysUserUpdateParam param);

    SysUserRow toRow(SysUser entity);
}
