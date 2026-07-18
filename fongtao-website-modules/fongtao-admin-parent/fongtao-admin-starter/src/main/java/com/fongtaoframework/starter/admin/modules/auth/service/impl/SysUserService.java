package com.fongtaoframework.starter.admin.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fongtaoframework.starter.admin.modules.auth.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.auth.mapper.SysUserMapper;
import com.fongtaoframework.starter.admin.modules.auth.service.ISysUserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SysUserService implements ISysUserService {

    private static final int ENABLED_STATUS = 1;
    private static final int NOT_DELETED = 0;

    private final SysUserMapper sysUserMapper;

    @Override
    public Optional<SysUser> findByUserCode(String userCode) {
        return Optional.ofNullable(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getSysUserCode, userCode)
                .eq(SysUser::getDeleted, NOT_DELETED)
                .last("limit 1")));
    }

    @Override
    public Optional<SysUser> findByUserId(String userId) {
        return Optional.ofNullable(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getSysUserId, userId)
                .eq(SysUser::getDeleted, NOT_DELETED)
                .last("limit 1")));
    }

    @Override
    public void assertEnabled(SysUser user) {
        if (!Integer.valueOf(ENABLED_STATUS).equals(user.getSysUserStatus())) {
            throw new DisabledException("用户已禁用");
        }
    }
}
