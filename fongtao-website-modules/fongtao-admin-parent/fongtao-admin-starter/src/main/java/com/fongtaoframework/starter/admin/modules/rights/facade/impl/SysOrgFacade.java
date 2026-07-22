package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import cn.hutool.core.util.StrUtil;
import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.core.util.TreeUtil;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysOrgConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysOrgRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysOrgFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysOrgService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysOrgFacade implements ISysOrgFacade {

    private final ISysOrgService sysOrgService;
    private final ISysRightsService sysRightsService;
    private final SysOrgConverter sysOrgConverter;

    @Override
    public PageResult<SysOrgRow> page(SysOrgPageParam param) {
        SysOrgPageParam pageParam = param == null ? new SysOrgPageParam(null, null) : param;
        PageResult<SysOrg> page = sysOrgService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysOrgConverter::toRow);
    }

    @Override
    public List<SysOrgRow> tree() {
        try {
            return TreeUtil.build(sysOrgService.list().stream().map(sysOrgConverter::toRow).toList(),
                    SysOrgRow::sysOrgId, SysOrgRow::parentId, SysOrgRow::withChildren);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("组织层级存在循环", exception);
        }
    }

    @Override
    public SysOrgRow getById(String sysOrgId) {
        return sysOrgConverter.toRow(require(sysOrgId));
    }

    @Override
    @Transactional
    public void create(SysOrgCreateParam param) {
        SysOrg entity = sysOrgConverter.toEntity(param);
        assertParent(entity.getSysOrgId(), entity.getParentId());
        assertCodeUnique(entity.getSysOrgCode(), null);
        if (!sysOrgService.save(entity)) {
            throw new BusinessException("组织新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysOrgUpdateParam param) {
        SysOrg entity = sysOrgConverter.toEntity(param);
        require(entity.getSysOrgId());
        assertParent(entity.getSysOrgId(), entity.getParentId());
        assertCodeUnique(entity.getSysOrgCode(), entity.getSysOrgId());
        if (!sysOrgService.updateById(entity)) {
            throw new BusinessException("组织更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysOrgId) {
        require(sysOrgId);
        if (sysOrgService.existsByParentId(sysOrgId)) {
            throw new BusinessException("组织存在下级，不能删除");
        }
        if (sysRightsService.existsBySysOrgId(sysOrgId) || sysRightsService.existsExtraBySysOrgId(sysOrgId)) {
            throw new BusinessException("组织仍被身份数据引用，不能删除");
        }
        if (!sysOrgService.deleteById(sysOrgId)) {
            throw new BusinessException("组织删除失败");
        }
    }

    private SysOrg require(String sysOrgId) {
        SysOrg entity = sysOrgService.findById(sysOrgId);
        if (entity == null) {
            throw new BusinessException("组织不存在或已删除");
        }
        return entity;
    }

    private void assertParent(String currentId, String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return;
        }
        if (StrUtil.equals(currentId, parentId)) {
            throw new BusinessException("组织不能设置自身为上级");
        }
        String parent = parentId;
        Set<String> visited = new HashSet<>();
        while (StrUtil.isNotBlank(parent)) {
            if (!visited.add(parent) || StrUtil.equals(currentId, parent)) {
                throw new BusinessException("组织层级存在循环");
            }
            parent = require(parent).getParentId();
        }
    }

    private void assertCodeUnique(String sysOrgCode, String currentId) {
        if (sysOrgService.existsByCode(sysOrgCode, currentId)) {
            throw new BusinessException("组织编码已存在");
        }
    }

}
