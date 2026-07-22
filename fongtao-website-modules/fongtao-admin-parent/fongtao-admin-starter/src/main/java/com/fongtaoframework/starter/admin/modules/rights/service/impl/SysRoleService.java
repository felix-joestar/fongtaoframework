package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysRoleService implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    public PageResult<SysRole> page(PageQuery pageQuery) {
        Page<SysRole> page = sysRoleMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getSortNo).orderByAsc(SysRole::getSysRoleCode));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<SysRole> list() {
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getSortNo)
                .orderByAsc(SysRole::getSysRoleCode));
    }

    @Override
    public SysRole findById(String sysRoleId) {
        return sysRoleMapper.selectById(sysRoleId);
    }

    @Override
    public boolean existsByCode(String sysRoleCode, String excludedSysRoleId) {
        return sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>().eq(SysRole::getSysRoleCode, sysRoleCode)
                .ne(excludedSysRoleId != null, SysRole::getSysRoleId, excludedSysRoleId)) > 0;
    }

    @Override
    public boolean existsByParentId(String parentId) {
        return sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>().eq(SysRole::getParentId, parentId)) > 0;
    }

    @Override
    public boolean save(SysRole entity) {
        return sysRoleMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysRole entity) {
        return sysRoleMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysRoleId) {
        return sysRoleMapper.deleteById(sysRoleId) == 1;
    }
}
