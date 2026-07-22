package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysResConverter;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysRoleAuthConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleAuthCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRoleAuthFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysResService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleAuthService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SysRoleAuthFacade implements ISysRoleAuthFacade {

    private final ISysRoleAuthService sysRoleAuthService;
    private final ISysRoleService sysRoleService;
    private final ISysResService sysResService;
    private final SysResConverter sysResConverter;
    private final SysRoleAuthConverter sysRoleAuthConverter;

    @Override
    public List<SysResRow> listAuthResources(String sysRoleId) {
        requireRole(sysRoleId);
        return sysResService.listByIds(sysRoleAuthService.listResourceIdsByRoleId(sysRoleId)).stream()
                .map(sysResConverter::toRow).toList();
    }

    @Override
    @Transactional
    public void replaceAuthResources(String sysRoleId, List<String> sysResIds) {
        requireRole(sysRoleId);
        List<String> distinctIds = sysResIds == null ? List.of() : new ArrayList<>(new LinkedHashSet<>(sysResIds));
        for (String sysResId : distinctIds) {
            if (sysResId == null || sysResId.isBlank() || sysResService.findById(sysResId) == null) {
                throw new BusinessException("资源不存在或已删除");
            }
        }
        List<SysRoleAuthCreateParam> params = distinctIds.stream()
                .map(sysResId -> new SysRoleAuthCreateParam(sysRoleId, sysResId)).toList();
        List<SysRoleAuth> entities = params.stream().map(sysRoleAuthConverter::toEntity).toList();
        if (!sysRoleAuthService.replaceByRoleId(sysRoleId, entities)) {
            throw new BusinessException("角色资源授权失败");
        }
    }

    private void requireRole(String sysRoleId) {
        if (sysRoleService.findById(sysRoleId) == null) {
            throw new BusinessException("角色不存在或已删除");
        }
    }
}
