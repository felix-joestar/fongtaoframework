package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import org.springframework.stereotype.Component;
import cn.hutool.core.util.IdUtil;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysUserConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysUserRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdatePasswordParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysUserUpdateStatusParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysUserFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Component
public class SysUserFacade implements ISysUserFacade {

    private final ISysUserService sysUserService;
    private final SysUserConverter sysUserConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<SysUserRow> page(SysUserPageParam param) {
        SysUserPageParam pageParam = param == null ? new SysUserPageParam(null, null) : param;
        PageResult<SysUser> page = sysUserService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysUserConverter::toRow).toList(), page.total(), page.pageNo(), page.pageSize());
    }

    @Override
    public SysUserRow getById(String sysUserId) {
        return sysUserConverter.toRow(sysUserService.get(sysUserId));
    }

    @Override
    public void create(SysUserCreateParam param) {
        SysUser user = sysUserConverter.toEntity(param);
        user.setSysUserId(IdUtil.simpleUUID());
        user.setSysUserPwd(passwordEncoder.encode(param.password()));
        sysUserService.save(user);
    }

    @Override
    public void updateById(SysUserUpdateParam param) {
        sysUserService.update(sysUserConverter.toEntity(param));
    }

    @Override
    public void deleteById(String sysUserId) {
        sysUserService.delete(sysUserId);
    }

    @Override
    public void updateStatus(SysUserUpdateStatusParam param) {
        sysUserService.updateStatus(param.sysUserId(), param.sysUserStatus());
    }

    @Override
    public void updatePassword(SysUserUpdatePasswordParam param) {
        if (param.password().isBlank()) {
            throw new BusinessException("密码不能为空");
        }
        sysUserService.resetPassword(param.sysUserId(), passwordEncoder.encode(param.password()));
    }
}
