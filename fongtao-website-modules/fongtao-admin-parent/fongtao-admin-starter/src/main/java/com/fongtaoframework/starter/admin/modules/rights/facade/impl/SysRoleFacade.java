package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import org.springframework.stereotype.Component;
import cn.hutool.core.util.IdUtil;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysRoleConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysRoleRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRolePageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRoleFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysRoleFacade implements ISysRoleFacade {

    private final ISysRoleService sysRoleService;
    private final SysRoleConverter sysRoleConverter;

    @Override
    public PageResult<SysRoleRow> page(SysRolePageParam param) {
        SysRolePageParam pageParam = param == null ? new SysRolePageParam(null, null) : param;
        PageResult<SysRole> page = sysRoleService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysRoleConverter::toRow).toList(), page.total(),
                page.pageNo(), page.pageSize());
    }

    @Override
    public List<SysRoleRow> tree() {
        List<SysRole> entities = sysRoleService.list();
        Map<String, SysRole> entityById = entities.stream()
                .collect(java.util.stream.Collectors.toMap(SysRole::getSysRoleId, entity -> entity, (left, right) -> left, LinkedHashMap::new));
        Map<String, List<SysRole>> childrenByParentId = new LinkedHashMap<>();
        List<SysRole> roots = new ArrayList<>();
        for (SysRole entity : entities) {
            if (entity.getParentId() == null || entity.getParentId().isBlank()) {
                roots.add(entity);
            } else {
                childrenByParentId.computeIfAbsent(entity.getParentId(), ignored -> new ArrayList<>()).add(entity);
            }
        }
        return roots.stream().map(entity -> toTreeRow(entity, childrenByParentId)).toList();
    }

    @Override
    public SysRoleRow getById(String sysRoleId) {
        return sysRoleConverter.toRow(sysRoleService.get(sysRoleId));
    }

    @Override
    public void create(SysRoleCreateParam param) {
        SysRole entity = sysRoleConverter.toEntity(param);
        entity.setSysRoleId(IdUtil.simpleUUID());
        sysRoleService.create(entity);
    }

    @Override
    public void updateById(SysRoleUpdateParam param) {
        sysRoleService.updateById(sysRoleConverter.toEntity(param));
    }

    @Override
    public void deleteById(String sysRoleId) {
        sysRoleService.deleteById(sysRoleId);
    }

    private SysRoleRow toTreeRow(SysRole entity, Map<String, List<SysRole>> childrenByParentId) {
        SysRoleRow row = sysRoleConverter.toRow(entity);
        List<SysRoleRow> children = childrenByParentId.getOrDefault(entity.getSysRoleId(), List.of()).stream()
                .map(child -> toTreeRow(child, childrenByParentId))
                .toList();
        return new SysRoleRow(
                row.sysRoleId(), row.parentId(), row.sysRoleCode(), row.sysRoleName(), row.enabled(),
                row.sortNo(), row.remark(), children);
    }
}
