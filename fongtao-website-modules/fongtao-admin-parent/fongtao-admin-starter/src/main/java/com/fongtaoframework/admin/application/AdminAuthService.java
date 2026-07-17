package com.fongtaoframework.admin.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fongtaoframework.admin.application.converter.SysUserConverter;
import com.fongtaoframework.admin.web.dto.LoginRequest;
import com.fongtaoframework.admin.web.dto.LoginResponse;
import com.fongtaoframework.admin.web.dto.LoginUserResponse;
import com.fongtaoframework.admin.web.dto.RefreshTokenRequest;
import com.fongtaoframework.admin.domain.entity.SysUser;
import com.fongtaoframework.admin.infrastructure.mapper.SysUserMapper;
import com.fongtaoframework.starter.security.jwt.JwtTokenService;
import com.fongtaoframework.starter.security.jwt.RefreshTokenPrincipal;
import com.fongtaoframework.starter.security.jwt.TokenValue;
import com.fongtaoframework.starter.security.userdetails.LoginUserDetails;
import com.fongtaoframework.starter.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AdminAuthService {

    private static final int ENABLED_STATUS = 1;
    private static final int NOT_DELETED = 0;

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final SysUserConverter sysUserConverter;

    public LoginResponse login(LoginRequest request) {
        SysUser user = findByUsername(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.getSysUserPwd())) {
            throw new BadCredentialsException("账号或密码错误");
        }
        assertEnabled(user);
        return createLoginResponse(user);
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenPrincipal principal = jwtTokenService.parseRefreshToken(request.refreshToken());
        SysUser user = findById(principal.userId());
        if (user == null) {
            throw new BadCredentialsException("refresh token 无效");
        }
        assertEnabled(user);
        return createLoginResponse(user);
    }

    public LoginUserResponse currentUser() {
        LoginUserDetails loginUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未登录或登录已过期"));
        SysUser user = findById(loginUser.getUserId());
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("当前用户不存在");
        }
        assertEnabled(user);
        return sysUserConverter.toLoginUserResponse(user);
    }

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

    private SysUser findByUsername(String username) {
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getSysUserCode, username)
                .eq(SysUser::getDeleted, NOT_DELETED)
                .last("limit 1"));
    }

    private SysUser findById(String userId) {
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getSysUserId, userId)
                .eq(SysUser::getDeleted, NOT_DELETED)
                .last("limit 1"));
    }

    private void assertEnabled(SysUser user) {
        if (!Integer.valueOf(ENABLED_STATUS).equals(user.getSysUserStatus())) {
            throw new DisabledException("用户已禁用");
        }
    }

}
