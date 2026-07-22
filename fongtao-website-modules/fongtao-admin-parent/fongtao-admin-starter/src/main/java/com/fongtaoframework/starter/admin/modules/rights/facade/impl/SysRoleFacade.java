package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import cn.hutool.core.util.StrUtil;
import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.core.util.TreeUtil;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysRoleConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRoleRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRolePageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRoleFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleAuthService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysRoleFacade implements ISysRoleFacade {

    private final ISysRoleService sysRoleService;
    private final ISysRightsService sysRightsService;
    private final ISysRoleAuthService sysRoleAuthService;
    private final SysRoleConverter sysRoleConverter;

    @Override
    public PageResult<SysRoleRow> page(SysRolePageParam param) {
        SysRolePageParam pageParam = param == null ? new SysRolePageParam(null, null) : param;
        PageResult<SysRole> page = sysRoleService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysRoleConverter::toRow);
    }

    @Override
    public List<SysRoleRow> tree() {
        try {
            return TreeUtil.build(sysRoleService.list().stream().map(sysRoleConverter::toRow).toList(),
                    SysRoleRow::sysRoleId, SysRoleRow::parentId, SysRoleRow::withChildren);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("角色层级存在循环", exception);
        }
    }

    @Override
    public SysRoleRow getById(String sysRoleId) {
        return sysRoleConverter.toRow(require(sysRoleId));
    }

    @Override
    @Transactional
    public void create(SysRoleCreateParam param) {
        SysRole entity = sysRoleConverter.toEntity(param);
        assertParent(entity.getSysRoleId(), entity.getParentId());
        assertCodeUnique(entity.getSysRoleCode(), null);
        if (!sysRoleService.save(entity)) {
            throw new BusinessException("角色新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysRoleUpdateParam param) {
        SysRole entity = sysRoleConverter.toEntity(param);
        require(entity.getSysRoleId());
        assertParent(entity.getSysRoleId(), entity.getParentId());
        assertCodeUnique(entity.getSysRoleCode(), entity.getSysRoleId());
        if (!sysRoleService.updateById(entity)) {
            throw new BusinessException("角色更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysRoleId) {
        require(sysRoleId);
        if (sysRoleService.existsByParentId(sysRoleId)) {
            throw new BusinessException("角色存在下级，不能删除");
        }
        if (sysRightsService.existsBySysRoleId(sysRoleId) || sysRoleAuthService.existsByRoleId(sysRoleId)) {
            throw new BusinessException("角色仍被身份或授权数据引用，不能删除");
        }
        if (!sysRoleService.deleteById(sysRoleId)) {
            throw new BusinessException("角色删除失败");
        }
    }

    private SysRole require(String sysRoleId) {
        SysRole entity = sysRoleService.findById(sysRoleId);
        if (entity == null) {
            throw new BusinessException("角色不存在或已删除");
        }
        return entity;
    }

    private void assertParent(String currentId, String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return;
        }
        if (StrUtil.equals(currentId, parentId)) {
            throw new BusinessException("角色不能设置自身为上级");
        }
        String parent = parentId;
        Set<String> visited = new HashSet<>();
        while (StrUtil.isNotBlank(parent)) {
            if (!visited.add(parent) || StrUtil.equals(currentId, parent)) {
                throw new BusinessException("角色层级存在循环");
            }
            parent = require(parent).getParentId();
        }
    }

    private void assertCodeUnique(String sysRoleCode, String currentId) {
        if (sysRoleService.existsByCode(sysRoleCode, currentId)) {
            throw new BusinessException("角色编码已存在");
        }
    }

}
