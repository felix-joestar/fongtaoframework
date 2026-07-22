package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import cn.hutool.core.util.StrUtil;
import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.core.util.TreeUtil;
import com.fongtaoframework.starter.admin.common.enums.ResourceType;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysResConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysResFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysResService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleAuthService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysResFacade implements ISysResFacade {

    private final ISysResService sysResService;
    private final ISysRoleAuthService sysRoleAuthService;
    private final SysResConverter sysResConverter;

    @Override
    public PageResult<SysResRow> page(SysResPageParam param) {
        SysResPageParam pageParam = param == null ? new SysResPageParam(null, null) : param;
        PageResult<SysRes> page = sysResService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysResConverter::toRow);
    }

    @Override
    public List<SysResRow> tree() {
        try {
            return TreeUtil.build(sysResService.list().stream().map(sysResConverter::toRow).toList(),
                    SysResRow::sysResId, SysResRow::parentId, SysResRow::withChildren);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("资源层级存在循环", exception);
        }
    }

    @Override
    public SysResRow getById(String sysResId) {
        return sysResConverter.toRow(require(sysResId));
    }

    @Override
    @Transactional
    public void create(SysResCreateParam param) {
        SysRes entity = sysResConverter.toEntity(param);
        assertParent(entity.getSysResId(), entity.getParentId());
        assertCodeUnique(entity.getSysResCode(), null);
        assertType(entity.getSysResType());
        if (!sysResService.save(entity)) {
            throw new BusinessException("资源新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysResUpdateParam param) {
        SysRes entity = sysResConverter.toEntity(param);
        require(entity.getSysResId());
        assertParent(entity.getSysResId(), entity.getParentId());
        assertCodeUnique(entity.getSysResCode(), entity.getSysResId());
        assertType(entity.getSysResType());
        if (!sysResService.updateById(entity)) {
            throw new BusinessException("资源更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysResId) {
        require(sysResId);
        if (sysResService.existsByParentId(sysResId)) {
            throw new BusinessException("资源存在下级，不能删除");
        }
        if (sysRoleAuthService.existsByResId(sysResId)) {
            throw new BusinessException("资源仍被角色授权引用，不能删除");
        }
        if (!sysResService.deleteById(sysResId)) {
            throw new BusinessException("资源删除失败");
        }
    }

    private SysRes require(String sysResId) {
        SysRes entity = sysResService.findById(sysResId);
        if (entity == null) {
            throw new BusinessException("资源不存在或已删除");
        }
        return entity;
    }

    private void assertParent(String currentId, String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return;
        }
        if (StrUtil.equals(currentId, parentId)) {
            throw new BusinessException("资源不能设置自身为上级");
        }
        String parent = parentId;
        Set<String> visited = new HashSet<>();
        while (StrUtil.isNotBlank(parent)) {
            if (!visited.add(parent) || StrUtil.equals(currentId, parent)) {
                throw new BusinessException("资源层级存在循环");
            }
            parent = require(parent).getParentId();
        }
    }

    private void assertCodeUnique(String sysResCode, String currentId) {
        if (sysResService.existsByCode(sysResCode, currentId)) {
            throw new BusinessException("资源编码已存在");
        }
    }

    private void assertType(String type) {
        if (!ResourceType.supports(type)) {
            throw new BusinessException("资源类型不支持");
        }
    }

}
