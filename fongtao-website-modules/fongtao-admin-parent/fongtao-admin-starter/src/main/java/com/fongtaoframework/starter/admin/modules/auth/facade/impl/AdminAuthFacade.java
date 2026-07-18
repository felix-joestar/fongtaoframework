package com.fongtaoframework.starter.admin.modules.auth.facade.impl;

import com.fongtaoframework.starter.admin.modules.auth.converter.SysUserConverter;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginRequest;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.LoginUserResponse;
import com.fongtaoframework.starter.admin.modules.auth.domain.dto.RefreshTokenRequest;
import com.fongtaoframework.starter.admin.modules.auth.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.auth.facade.IAdminAuthFacade;
import com.fongtaoframework.starter.admin.modules.auth.service.ISysUserService;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import com.fongtaoframework.starter.security.jwt.RefreshTokenPrincipal;
import com.fongtaoframework.starter.security.jwt.TokenValue;
import com.fongtaoframework.starter.security.userdetails.LoginUserDetails;
import com.fongtaoframework.starter.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AdminAuthFacade implements IAdminAuthFacade {

    private final ISysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final SysUserConverter sysUserConverter;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserService.findByUserCode(request.username()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.password(), user.getSysUserPwd())) {
            throw new BadCredentialsException("账号或密码错误");
        }
        sysUserService.assertEnabled(user);
        return createLoginResponse(user);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenPrincipal principal = jwtTokenService.parseRefreshToken(request.refreshToken());
        SysUser user = sysUserService.findByUserId(principal.userId()).orElse(null);
        if (user == null) {
            throw new BadCredentialsException("refresh token 无效");
        }
        sysUserService.assertEnabled(user);
        return createLoginResponse(user);
    }

    @Override
    public LoginUserResponse loginUser() {
        LoginUserDetails loginUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未登录或登录已过期"));
        SysUser user = sysUserService.findByUserId(loginUser.getUserId())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("当前用户不存在"));
        sysUserService.assertEnabled(user);
        return sysUserConverter.toLoginUserResponse(user);
    }

    @Override
    public void logout() {
        // 最小登录版本不维护服务端 token 黑名单，由客户端删除 token 完成退出。
    }

    private LoginResponse createLoginResponse(SysUser user) {
        LoginUserDetails loginUser = sysUserConverter.toLoginUser(user);
        TokenValue accessToken = jwtTokenService.createAccessToken(loginUser);
        TokenValue refreshToken = jwtTokenService.createRefreshToken(loginUser);
        return new LoginResponse(
                accessToken.token(),
                accessToken.expiresIn(),
                refreshToken.token(),
                refreshToken.expiresIn(),
                sysUserConverter.toLoginUserResponse(user));
    }
}
