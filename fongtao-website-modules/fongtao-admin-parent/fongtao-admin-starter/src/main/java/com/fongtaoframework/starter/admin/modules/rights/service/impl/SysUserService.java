package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysUserMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysUserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysUserService implements ISysUserService {

    private static final int NOT_DELETED = 0;

    private final SysUserMapper sysUserMapper;

    @Override
    public Optional<SysUser> findByUserCode(String userCode) {
        return Optional.ofNullable(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getSysUserCode, userCode).eq(SysUser::getDeleted, NOT_DELETED).last("limit 1")));
    }

    @Override
    public Optional<SysUser> findByUserId(String userId) {
        return Optional.ofNullable(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getSysUserId, userId).eq(SysUser::getDeleted, NOT_DELETED).last("limit 1")));
    }

    @Override
    public PageResult<SysUser> page(PageQuery pageQuery) {
        Page<SysUser> page = sysUserMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeleted, NOT_DELETED)
                        .orderByDesc(SysUser::getCreateTime));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public boolean existsByUserCode(String sysUserCode, String excludedSysUserId) {
        return sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getSysUserCode, sysUserCode)
                .ne(excludedSysUserId != null, SysUser::getSysUserId, excludedSysUserId)) > 0;
    }

    @Override
    public boolean save(SysUser user) {
        return sysUserMapper.insert(user) == 1;
    }

    @Override
    public boolean updateById(SysUser user) {
        return sysUserMapper.updateById(user) == 1;
    }

    @Override
    public boolean deleteById(String sysUserId) {
        return sysUserMapper.deleteById(sysUserId) == 1;
    }
}
