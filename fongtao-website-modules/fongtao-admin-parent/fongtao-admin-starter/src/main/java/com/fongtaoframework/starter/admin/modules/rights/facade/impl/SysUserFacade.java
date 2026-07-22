package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysUserConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysUserRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdatePasswordParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateStatusParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysUserFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysUserFacade implements ISysUserFacade {

    private final ISysUserService sysUserService;
    private final ISysRightsService sysRightsService;
    private final SysUserConverter sysUserConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<SysUserRow> page(SysUserPageParam param) {
        SysUserPageParam pageParam = param == null ? new SysUserPageParam(null, null) : param;
        PageResult<SysUser> page = sysUserService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysUserConverter::toRow);
    }

    @Override
    public SysUserRow getById(String sysUserId) {
        return sysUserConverter.toRow(require(sysUserId));
    }

    @Override
    @Transactional
    public void create(SysUserCreateParam param) {
        SysUser user = sysUserConverter.toEntity(param);
        assertUserCodeUnique(user.getSysUserCode(), null);
        user.setSysUserPwd(passwordEncoder.encode(param.password()));
        if (!sysUserService.save(user)) {
            throw new BusinessException("用户新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysUserUpdateParam param) {
        SysUser current = require(param.sysUserId());
        SysUser user = sysUserConverter.toEntity(param);
        assertUserCodeUnique(user.getSysUserCode(), user.getSysUserId());
        user.setSysUserPwd(current.getSysUserPwd());
        user.setSysData(current.getSysData());
        if (!sysUserService.updateById(user)) {
            throw new BusinessException("用户更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysUserId) {
        require(sysUserId);
        if (sysRightsService.existsBySysUserId(sysUserId)) {
            throw new BusinessException("用户仍被身份数据引用，不能删除");
        }
        if (!sysUserService.deleteById(sysUserId)) {
            throw new BusinessException("用户删除失败");
        }
    }

    @Override
    @Transactional
    public void updateStatus(SysUserUpdateStatusParam param) {
        if (!Integer.valueOf(0).equals(param.sysUserStatus()) && !Integer.valueOf(1).equals(param.sysUserStatus())) {
            throw new BusinessException("用户状态不合法");
        }
        SysUser user = require(param.sysUserId());
        user.setSysUserStatus(param.sysUserStatus());
        user.setVersion(param.version());
        if (!sysUserService.updateById(user)) {
            throw new BusinessException("用户状态更新失败");
        }
    }

    @Override
    @Transactional
    public void updatePassword(SysUserUpdatePasswordParam param) {
        if (param.password().isBlank()) {
            throw new BusinessException("密码不能为空");
        }
        SysUser user = require(param.sysUserId());
        user.setSysUserPwd(passwordEncoder.encode(param.password()));
        user.setVersion(param.version());
        if (!sysUserService.updateById(user)) {
            throw new BusinessException("重置密码失败");
        }
    }

    private SysUser require(String sysUserId) {
        return sysUserService.findByUserId(sysUserId).orElseThrow(() -> new BusinessException("用户不存在或已删除"));
    }

    private void assertUserCodeUnique(String sysUserCode, String currentId) {
        if (sysUserService.existsByUserCode(sysUserCode, currentId)) {
            throw new BusinessException("用户账号已存在");
        }
    }
}
