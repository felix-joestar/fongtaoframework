package com.fongtaoframework.starter.admin.modules.auth.facade.impl;

import org.springframework.stereotype.Component;
import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.util.TreeUtil;
import com.fongtaoframework.starter.admin.modules.auth.converter.LoginUserConverter;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginRequest;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginIdentityResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginUserResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.RefreshTokenRequest;
import com.fongtaoframework.starter.admin.modules.auth.facade.IAdminAuthFacade;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysResConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleAuthService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysUserService;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import com.fongtaoframework.starter.security.jwt.RefreshTokenPrincipal;
import com.fongtaoframework.starter.security.jwt.TokenValue;
import com.fongtaoframework.starter.security.userdetails.LoginUserDetails;
import com.fongtaoframework.starter.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AdminAuthFacade implements IAdminAuthFacade {

    private final ISysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final LoginUserConverter loginUserConverter;
    private final ISysRightsService sysRightsService;
    private final ISysRoleAuthService sysRoleAuthService;
    private final SysResConverter sysResConverter;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserService.findByUserCode(request.username()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.password(), user.getSysUserPwd())) {
            throw new BadCredentialsException("账号或密码错误");
        }
        assertEnabled(user);
        return createLoginResponse(user);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenPrincipal principal = jwtTokenService.parseRefreshToken(request.refreshToken());
        SysUser user = sysUserService.findByUserId(principal.userId()).orElse(null);
        if (user == null) {
            throw new BadCredentialsException("refresh token 无效");
        }
        assertEnabled(user);
        return createLoginResponse(user);
    }

    @Override
    public LoginUserResponse loginUser() {
        SysUser user = currentUser();
        return loginUserConverter.toLoginUserResponse(user, permissions(user), defaultIdentity(user));
    }

    @Override
    public List<SysResRow> loginUserResources() {
        List<SysResRow> rows = sysRoleAuthService.listEffectiveResourcesByUserId(currentUser().getSysUserId()).stream()
                .map(sysResConverter::toRow)
                .toList();
        try {
            return TreeUtil.build(rows, SysResRow::sysResId, SysResRow::parentId, SysResRow::withChildren);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("资源层级存在循环", exception);
        }
    }

    @Override
    public void logout() {
        // 最小登录版本不维护服务端 token 黑名单，由客户端删除 token 完成退出。
    }

    private SysUser currentUser() {
        LoginUserDetails loginUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未登录或登录已过期"));
        SysUser user = sysUserService.findByUserId(loginUser.getUserId())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("当前用户不存在"));
        assertEnabled(user);
        return user;
    }

    private void assertEnabled(SysUser user) {
        if (!Integer.valueOf(1).equals(user.getSysUserStatus())) {
            throw new org.springframework.security.authentication.DisabledException("用户已禁用");
        }
    }

    private LoginResponse createLoginResponse(SysUser user) {
        List<String> permissions = permissions(user);
        LoginUserDetails loginUser = loginUserConverter.toLoginUser(user, permissions);
        TokenValue accessToken = jwtTokenService.createAccessToken(loginUser);
        TokenValue refreshToken = jwtTokenService.createRefreshToken(loginUser);
        return new LoginResponse(
                accessToken.token(),
                accessToken.expiresIn(),
                refreshToken.token(),
                refreshToken.expiresIn(),
                loginUserConverter.toLoginUserResponse(user, permissions, defaultIdentity(user)));
    }

    private List<String> permissions(SysUser user) {
        defaultRights(user);
        return sysRoleAuthService.listEffectivePermissionCodesByUserId(user.getSysUserId());
    }

    private LoginIdentityResponse defaultIdentity(SysUser user) {
        SysRights identity = defaultRights(user);
        return loginUserConverter.toLoginIdentityResponse(
                identity, sysRightsService.listCustomOrgIds(identity.getSysRightsId()));
    }

    private SysRights defaultRights(SysUser user) {
        SysRights identity = sysRightsService.findDefaultByUserId(user.getSysUserId());
        if (identity == null) {
            throw new BadCredentialsException("用户未配置启用的默认身份");
        }
        return identity;
    }
}
