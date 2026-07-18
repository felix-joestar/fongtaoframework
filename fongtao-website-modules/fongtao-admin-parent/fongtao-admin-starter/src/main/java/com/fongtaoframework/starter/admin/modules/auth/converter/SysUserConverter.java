package com.fongtaoframework.starter.admin.modules.auth.converter;

import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginUserResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.entity.SysUser;
import com.fongtaoframework.starter.security.userdetails.LoginUserDetails;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        implementationName = "<CLASS_NAME>Generated",
        implementationPackage = "<PACKAGE_NAME>.impl",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SysUserConverter {

    @Mapping(target = "userId", source = "sysUserId")
    @Mapping(target = "username", source = "sysUserCode")
    @Mapping(target = "name", source = "sysUserName")
    @Mapping(target = "mobile", source = "sysUserMobile")
    @Mapping(target = "email", source = "sysUserEmail")
    @Mapping(target = "permissions", expression = "java(java.util.List.of())")
    LoginUserResponse toLoginUserResponse(SysUser user);

    default LoginUserDetails toLoginUser(SysUser user) {
        return new LoginUserDetails(
                user.getSysUserId(),
                user.getSysUserCode(),
                user.getSysUserName(),
                user.getSysUserMobile(),
                user.getSysUserEmail(),
                user.getAvatarFileId(),
                List.of());
    }
}
