package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleAuthMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleService;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysRoleService implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRightsMapper sysRightsMapper;
    private final SysRoleAuthMapper sysRoleAuthMapper;

    @Override
    public PageResult<SysRole> page(PageQuery pageQuery) {
        Page<SysRole> page = sysRoleMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getSortNo)
                        .orderByAsc(SysRole::getSysRoleCode));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<SysRole> list() {
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .orderByAsc(SysRole::getSortNo)
                .orderByAsc(SysRole::getSysRoleCode));
    }

    @Override
    public SysRole get(String sysRoleId) {
        SysRole entity = sysRoleMapper.selectById(sysRoleId);
        if (entity == null) {
            throw new BusinessException("角色不存在或已删除");
        }
        return entity;
    }

    @Override
    @Transactional
    public void create(SysRole entity) {
        assertParent(entity.getSysRoleId(), entity.getParentId());
        assertCodeUnique(entity.getSysRoleCode(), null);
        if (sysRoleMapper.insert(entity) != 1) {
            throw new BusinessException("角色新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysRole entity) {
        get(entity.getSysRoleId());
        assertParent(entity.getSysRoleId(), entity.getParentId());
        assertCodeUnique(entity.getSysRoleCode(), entity.getSysRoleId());
        if (sysRoleMapper.updateById(entity) != 1) {
            throw new BusinessException("角色更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysRoleId) {
        get(sysRoleId);
        if (sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>().eq(SysRole::getParentId, sysRoleId)) > 0) {
            throw new BusinessException("角色存在下级，不能删除");
        }
        if (sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>().eq(SysRights::getSysRoleId, sysRoleId)) > 0
                || sysRoleAuthMapper.selectCount(new LambdaQueryWrapper<SysRoleAuth>()
                .eq(SysRoleAuth::getSysRoleId, sysRoleId)) > 0) {
            throw new BusinessException("角色仍被身份或授权数据引用，不能删除");
        }
        if (sysRoleMapper.deleteById(sysRoleId) != 1) {
            throw new BusinessException("角色删除失败");
        }
    }

    private void assertParent(String currentId, String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return;
        }
        if (StrUtil.equals(currentId, parentId)) {
            throw new BusinessException("角色不能设置自身为上级");
        }
        String parent = parentId;
        Set<String> visited = new LinkedHashSet<>();
        while (StrUtil.isNotBlank(parent)) {
            if (!visited.add(parent) || StrUtil.equals(currentId, parent)) {
                throw new BusinessException("角色层级存在循环");
            }
            parent = get(parent).getParentId();
        }
    }

    private void assertCodeUnique(String sysRoleCode, String currentId) {
        if (sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getSysRoleCode, sysRoleCode)
                .ne(StrUtil.isNotBlank(currentId), SysRole::getSysRoleId, currentId)) > 0) {
            throw new BusinessException("角色编码已存在");
        }
    }
}
