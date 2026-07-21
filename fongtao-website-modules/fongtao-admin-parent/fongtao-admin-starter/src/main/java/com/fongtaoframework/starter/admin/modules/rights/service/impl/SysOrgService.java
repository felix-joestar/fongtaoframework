package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRightsExtra;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysOrgMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsExtraMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysOrgService;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysOrgService implements ISysOrgService {

    private final SysOrgMapper sysOrgMapper;
    private final SysRightsMapper sysRightsMapper;
    private final SysRightsExtraMapper sysRightsExtraMapper;

    @Override
    public PageResult<SysOrg> page(PageQuery pageQuery) {
        Page<SysOrg> page = sysOrgMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysOrg>().orderByAsc(SysOrg::getSortNo).orderByAsc(SysOrg::getSysOrgCode));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<SysOrg> list() {
        return sysOrgMapper.selectList(new LambdaQueryWrapper<SysOrg>()
                .orderByAsc(SysOrg::getSortNo)
                .orderByAsc(SysOrg::getSysOrgCode));
    }

    @Override
    public SysOrg get(String sysOrgId) {
        SysOrg entity = sysOrgMapper.selectById(sysOrgId);
        if (entity == null) {
            throw new BusinessException("组织不存在或已删除");
        }
        return entity;
    }

    @Override
    @Transactional
    public void create(SysOrg entity) {
        assertParent(entity.getSysOrgId(), entity.getParentId());
        assertCodeUnique(entity.getSysOrgCode(), null);
        if (sysOrgMapper.insert(entity) != 1) {
            throw new BusinessException("组织新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysOrg entity) {
        get(entity.getSysOrgId());
        assertParent(entity.getSysOrgId(), entity.getParentId());
        assertCodeUnique(entity.getSysOrgCode(), entity.getSysOrgId());
        if (sysOrgMapper.updateById(entity) != 1) {
            throw new BusinessException("组织更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysOrgId) {
        get(sysOrgId);
        if (sysOrgMapper.selectCount(new LambdaQueryWrapper<SysOrg>().eq(SysOrg::getParentId, sysOrgId)) > 0) {
            throw new BusinessException("组织存在下级，不能删除");
        }
        if (sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>().eq(SysRights::getSysOrgId, sysOrgId)) > 0
                || sysRightsExtraMapper.selectCount(new LambdaQueryWrapper<SysRightsExtra>()
                .eq(SysRightsExtra::getSysOrgId, sysOrgId)) > 0) {
            throw new BusinessException("组织仍被身份数据引用，不能删除");
        }
        if (sysOrgMapper.deleteById(sysOrgId) != 1) {
            throw new BusinessException("组织删除失败");
        }
    }

    private void assertParent(String currentId, String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return;
        }
        if (StrUtil.equals(currentId, parentId)) {
            throw new BusinessException("组织不能设置自身为上级");
        }
        String parent = parentId;
        Set<String> visited = new LinkedHashSet<>();
        while (StrUtil.isNotBlank(parent)) {
            if (!visited.add(parent) || StrUtil.equals(currentId, parent)) {
                throw new BusinessException("组织层级存在循环");
            }
            parent = get(parent).getParentId();
        }
    }

    private void assertCodeUnique(String sysOrgCode, String currentId) {
        if (sysOrgMapper.selectCount(new LambdaQueryWrapper<SysOrg>()
                .eq(SysOrg::getSysOrgCode, sysOrgCode)
                .ne(StrUtil.isNotBlank(currentId), SysOrg::getSysOrgId, currentId)) > 0) {
            throw new BusinessException("组织编码已存在");
        }
    }
}
