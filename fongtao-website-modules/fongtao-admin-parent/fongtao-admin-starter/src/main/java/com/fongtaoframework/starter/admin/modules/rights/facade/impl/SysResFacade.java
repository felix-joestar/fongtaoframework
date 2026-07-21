package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import org.springframework.stereotype.Component;
import cn.hutool.core.util.IdUtil;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysResConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResPageParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysResUpdateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysResFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysResService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysResFacade implements ISysResFacade {

    private final ISysResService sysResService;
    private final SysResConverter sysResConverter;

    @Override
    public PageResult<SysResRow> page(SysResPageParam param) {
        SysResPageParam pageParam = param == null ? new SysResPageParam(null, null) : param;
        PageResult<SysRes> page = sysResService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return PageResult.of(page.records().stream().map(sysResConverter::toRow).toList(), page.total(),
                page.pageNo(), page.pageSize());
    }

    @Override
    public List<SysResRow> tree() {
        Map<String, List<SysRes>> children = new LinkedHashMap<>();
        List<SysRes> roots = new ArrayList<>();
        for (SysRes entity : sysResService.list()) {
            if (entity.getParentId() == null || entity.getParentId().isBlank()) {
                roots.add(entity);
            } else {
                children.computeIfAbsent(entity.getParentId(), ignored -> new ArrayList<>()).add(entity);
            }
        }
        return roots.stream().map(entity -> toTreeRow(entity, children)).toList();
    }

    @Override
    public SysResRow getById(String sysResId) { return sysResConverter.toRow(sysResService.get(sysResId)); }

    @Override
    public void create(SysResCreateParam param) {
        SysRes entity = sysResConverter.toEntity(param);
        entity.setSysResId(IdUtil.simpleUUID());
        sysResService.create(entity);
    }

    @Override
    public void updateById(SysResUpdateParam param) { sysResService.updateById(sysResConverter.toEntity(param)); }

    @Override
    public void deleteById(String sysResId) { sysResService.deleteById(sysResId); }

    private SysResRow toTreeRow(SysRes entity, Map<String, List<SysRes>> childrenByParent) {
        SysResRow row = sysResConverter.toRow(entity);
        List<SysResRow> children = childrenByParent.getOrDefault(entity.getSysResId(), List.of()).stream()
                .map(child -> toTreeRow(child, childrenByParent)).toList();
        return new SysResRow(row.sysResId(), row.parentId(), row.sysResCode(), row.sysResName(), row.sysResType(),
                row.permissionCode(), row.routePath(), row.componentPath(), row.icon(), row.visibled(), row.enabled(),
                row.sortNo(), row.sysData(), row.remark(), children);
    }
}
