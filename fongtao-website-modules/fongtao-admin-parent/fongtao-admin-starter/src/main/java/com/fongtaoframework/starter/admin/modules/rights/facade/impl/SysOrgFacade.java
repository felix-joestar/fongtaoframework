package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import org.springframework.stereotype.Component;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysOrgConverter;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysOrgRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysOrgUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysOrgFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysOrgService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysOrgFacade implements ISysOrgFacade {

    private final ISysOrgService sysOrgService;
    private final SysOrgConverter sysOrgConverter;

    @Override
    public PageResult<SysOrgRow> page(SysOrgPageParam param) {
        SysOrgPageParam pageParam = param == null ? new SysOrgPageParam(1L, 20L) : param;
        PageResult<SysOrg> page = sysOrgService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysOrgConverter::toRow).toList(), page.total(),
                page.pageNo(), page.pageSize());
    }

    @Override
    public List<SysOrgRow> tree() {
        Map<String, List<SysOrgRow>> childrenByParentId = new LinkedHashMap<>();
        List<SysOrgRow> roots = new ArrayList<>();
        for (SysOrg entity : sysOrgService.list()) {
            SysOrgRow row = sysOrgConverter.toRow(entity);
            childrenByParentId.computeIfAbsent(entity.getParentId(), ignored -> new ArrayList<>()).add(row);
        }
        for (SysOrg entity : sysOrgService.list()) {
            if (entity.getParentId() == null || entity.getParentId().isBlank()) {
                roots.add(toTreeRow(entity, childrenByParentId));
            }
        }
        return roots;
    }

    @Override
    public SysOrgRow getById(String sysOrgId) {
        return sysOrgConverter.toRow(sysOrgService.get(sysOrgId));
    }

    @Override
    public void create(SysOrgCreateParam param) {
        SysOrg entity = sysOrgConverter.toEntity(param);
        entity.setSysOrgId(IdUtil.simpleUUID());
        sysOrgService.create(entity);
    }

    @Override
    public void updateById(SysOrgUpdateParam param) {
        sysOrgService.updateById(sysOrgConverter.toEntity(param));
    }

    @Override
    public void deleteById(String sysOrgId) {
        sysOrgService.deleteById(sysOrgId);
    }

    private SysOrgRow toTreeRow(SysOrg entity, Map<String, List<SysOrgRow>> childrenByParentId) {
        SysOrgRow row = sysOrgConverter.toRow(entity);
        List<SysOrgRow> children = childrenByParentId.getOrDefault(entity.getSysOrgId(), List.of()).stream()
                .map(child -> toTreeRow(sysOrgService.get(child.sysOrgId()), childrenByParentId))
                .toList();
        return new SysOrgRow(
                row.sysOrgId(), row.parentId(), row.sysOrgCode(), row.sysOrgName(), row.sysOrgType(),
                row.enabled(), row.sortNo(), row.remark(), children);
    }
}
