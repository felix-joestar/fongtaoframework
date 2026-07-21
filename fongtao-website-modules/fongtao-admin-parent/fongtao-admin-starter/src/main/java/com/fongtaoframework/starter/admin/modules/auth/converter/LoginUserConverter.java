package com.fongtaoframework.starter.admin.modules.auth.converter;

import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginUserResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginIdentityResponse;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
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
public interface LoginUserConverter {

    @Mapping(target = "userId", source = "user.sysUserId")
    @Mapping(target = "username", source = "user.sysUserCode")
    @Mapping(target = "name", source = "user.sysUserName")
    @Mapping(target = "mobile", source = "user.sysUserMobile")
    @Mapping(target = "email", source = "user.sysUserEmail")
    LoginUserResponse toLoginUserResponse(
            SysUser user, List<String> permissions, LoginIdentityResponse defaultIdentity);

    @Mapping(target = "rightsId", source = "identity.sysRightsId")
    @Mapping(target = "orgId", source = "identity.sysOrgId")
    @Mapping(target = "roleId", source = "identity.sysRoleId")
    @Mapping(target = "orgIds", source = "orgIds")
    LoginIdentityResponse toLoginIdentityResponse(SysRights identity, List<String> orgIds);

    default LoginUserDetails toLoginUser(SysUser user, List<String> permissions) {
        return new LoginUserDetails(
                user.getSysUserId(),
                user.getSysUserCode(),
                user.getSysUserName(),
                user.getSysUserMobile(),
                user.getSysUserEmail(),
                user.getAvatarFileId(),
                permissions.stream().map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).toList());
    }
}
