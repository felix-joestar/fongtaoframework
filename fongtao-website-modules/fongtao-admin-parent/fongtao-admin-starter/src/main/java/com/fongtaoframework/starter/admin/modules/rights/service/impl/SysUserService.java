package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysUserMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysUserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysUserService implements ISysUserService {

    private static final int ENABLED_STATUS = 1;
    private static final int NOT_DELETED = 0;

    private final SysUserMapper sysUserMapper;
    private final SysRightsMapper sysRightsMapper;

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
    public PageResult<SysUser> page(PageQuery pageQuery) {
        Page<SysUser> page = sysUserMapper.selectPage(
                new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getDeleted, NOT_DELETED)
                        .orderByDesc(SysUser::getCreateTime));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysUser get(String userId) {
        return findByUserId(userId).orElseThrow(() -> new BusinessException("用户不存在或已删除"));
    }

    @Override
    @Transactional
    public void save(SysUser user) {
        assertUserCodeUnique(user.getSysUserCode(), null);
        if (sysUserMapper.insert(user) != 1) {
            throw new BusinessException("用户新增失败");
        }
    }

    @Override
    @Transactional
    public void update(SysUser user) {
        SysUser current = get(user.getSysUserId());
        assertUserCodeUnique(user.getSysUserCode(), user.getSysUserId());
        user.setSysUserPwd(current.getSysUserPwd());
        user.setSysData(current.getSysData());
        user.setVersion(current.getVersion());
        if (sysUserMapper.updateById(user) != 1) {
            throw new BusinessException("用户更新失败");
        }
    }

    @Override
    @Transactional
    public void delete(String userId) {
        get(userId);
        if (sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>()
                .eq(SysRights::getSysUserId, userId)) > 0) {
            throw new BusinessException("用户仍被身份数据引用，不能删除");
        }
        if (sysUserMapper.deleteById(userId) != 1) {
            throw new BusinessException("用户删除失败");
        }
    }

    @Override
    @Transactional
    public void updateStatus(String userId, Integer status) {
        if (!Integer.valueOf(0).equals(status) && !Integer.valueOf(ENABLED_STATUS).equals(status)) {
            throw new BusinessException("用户状态不合法");
        }
        SysUser user = get(userId);
        user.setSysUserStatus(status);
        if (sysUserMapper.updateById(user) != 1) {
            throw new BusinessException("用户状态更新失败");
        }
    }

    @Override
    @Transactional
    public void resetPassword(String userId, String encodedPassword) {
        SysUser user = get(userId);
        user.setSysUserPwd(encodedPassword);
        if (sysUserMapper.updateById(user) != 1) {
            throw new BusinessException("重置密码失败");
        }
    }

    @Override
    public void assertEnabled(SysUser user) {
        if (!Integer.valueOf(ENABLED_STATUS).equals(user.getSysUserStatus())) {
            throw new DisabledException("用户已禁用");
        }
    }

    private void assertUserCodeUnique(String userCode, String currentId) {
        if (sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getSysUserCode, userCode)
                .ne(cn.hutool.core.util.StrUtil.isNotBlank(currentId), SysUser::getSysUserId, currentId)) > 0) {
            throw new BusinessException("用户账号已存在");
        }
    }
}
