package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import cn.hutool.core.collection.CollUtil;
import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.common.enums.DataScope;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysRightsConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRightsRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateEnabledParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRightsUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRightsFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysOrgService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysRightsFacade implements ISysRightsFacade {

    private final ISysRightsService sysRightsService;
    private final ISysUserService sysUserService;
    private final ISysOrgService sysOrgService;
    private final ISysRoleService sysRoleService;
    private final SysRightsConverter sysRightsConverter;

    @Override
    public PageResult<SysRightsRow> page(SysRightsPageParam param) {
        SysRightsPageParam pageParam = param == null ? new SysRightsPageParam(null, null) : param;
        PageResult<SysRights> page = sysRightsService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(this::toRow);
    }

    @Override
    public SysRightsRow getById(String sysRightsId) {
        return toRow(require(sysRightsId));
    }

    @Override
    @Transactional
    public void create(SysRightsCreateParam param) {
        SysRights entity = sysRightsConverter.toEntity(param);
        validate(entity, param.customSysOrgIds(), null);
        if (Integer.valueOf(1).equals(entity.getDefaulted())) {
            sysRightsService.clearDefaulted(entity.getSysUserId(), null);
        }
        if (!sysRightsService.save(entity) || !replaceCustomSysOrgs(entity.getSysRightsId(), param.customSysOrgIds())) {
            throw new BusinessException("身份新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysRightsUpdateParam param) {
        SysRights entity = sysRightsConverter.toEntity(param);
        require(entity.getSysRightsId());
        validate(entity, param.customSysOrgIds(), entity.getSysRightsId());
        if (Integer.valueOf(1).equals(entity.getDefaulted())) {
            sysRightsService.clearDefaulted(entity.getSysUserId(), entity.getSysRightsId());
        }
        if (!sysRightsService.updateById(entity) || !replaceCustomSysOrgs(entity.getSysRightsId(), param.customSysOrgIds())) {
            throw new BusinessException("身份更新失败");
        }
    }

    @Override
    @Transactional
    public void updateEnabled(SysRightsUpdateEnabledParam param) {
        if (!Integer.valueOf(0).equals(param.enabled()) && !Integer.valueOf(1).equals(param.enabled())) {
            throw new BusinessException("身份状态不合法");
        }
        SysRights entity = require(param.sysRightsId());
        entity.setEnabled(param.enabled());
        entity.setVersion(param.version());
        if (!sysRightsService.updateById(entity)) {
            throw new BusinessException("身份状态更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysRightsId) {
        require(sysRightsId);
        if (!sysRightsService.deleteById(sysRightsId)) {
            throw new BusinessException("身份删除失败");
        }
    }

    private SysRights require(String sysRightsId) {
        SysRights entity = sysRightsService.findById(sysRightsId);
        if (entity == null) {
            throw new BusinessException("身份不存在或已删除");
        }
        return entity;
    }

    private void validate(SysRights entity, List<String> customSysOrgIds, String currentId) {
        if (sysUserService.findByUserId(entity.getSysUserId()).isEmpty()
                || sysOrgService.findById(entity.getSysOrgId()) == null
                || sysRoleService.findById(entity.getSysRoleId()) == null) {
            throw new BusinessException("用户、组织或角色不存在或已删除");
        }
        if (!DataScope.supports(entity.getDataScope())) {
            throw new BusinessException("数据范围不合法");
        }
        if (sysRightsService.existsByUserOrgRole(entity.getSysUserId(), entity.getSysOrgId(), entity.getSysRoleId(), currentId)) {
            throw new BusinessException("用户组织角色身份已存在");
        }
        if (DataScope.CUSTOM.getCode().equals(entity.getDataScope()) && CollUtil.isEmpty(customSysOrgIds)) {
            throw new BusinessException("自定义数据范围必须指定组织");
        }
        for (String sysOrgId : CollUtil.distinct(customSysOrgIds == null ? List.of() : customSysOrgIds)) {
            if (cn.hutool.core.util.StrUtil.isBlank(sysOrgId) || sysOrgService.findById(sysOrgId) == null) {
                throw new BusinessException("自定义数据范围组织不存在或已删除");
            }
        }
    }

    private boolean replaceCustomSysOrgs(String sysRightsId, List<String> customSysOrgIds) {
        return sysRightsService.replaceCustomSysOrgs(sysRightsId, customSysOrgIds);
    }

    private SysRightsRow toRow(SysRights entity) {
        SysRightsRow row = sysRightsConverter.toRow(entity);
        return new SysRightsRow(row.sysRightsId(), row.sysUserId(), row.sysOrgId(), row.sysRoleId(), row.defaulted(),
                row.enabled(), row.dataScope(), row.remark(), sysRightsService.listCustomOrgIds(entity.getSysRightsId()),
                row.version());
    }
}
