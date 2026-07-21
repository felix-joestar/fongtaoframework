package com.fongtaoframework.starter.admin.modules.rights.facade.impl;

import org.springframework.stereotype.Component;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysResConverter;
import com.fongtaoframework.starter.admin.modules.rights.converter.SysRoleAuthConverter;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.SysResRow;
import com.fongtaoframework.starter.admin.modules.rights.domain.dto.param.SysRoleAuthCreateParam;
import com.fongtaoframework.starter.admin.modules.rights.facade.ISysRoleAuthFacade;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleAuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SysRoleAuthFacade implements ISysRoleAuthFacade {

    private final ISysRoleAuthService sysRoleAuthService;
    private final SysResConverter sysResConverter;
    private final SysRoleAuthConverter sysRoleAuthConverter;

    @Override
    public List<SysResRow> listAuthResources(String sysRoleId) {
        return sysRoleAuthService.listAuthResources(sysRoleId).stream().map(sysResConverter::toRow).toList();
    }

    @Override
    public void replaceAuthResources(String sysRoleId, List<String> sysResIds) {
        List<SysRoleAuthCreateParam> params = sysResIds == null ? List.of()
                : sysResIds.stream().map(sysResId -> new SysRoleAuthCreateParam(sysRoleId, sysResId)).toList();
        sysRoleAuthService.replaceAuthResources(sysRoleId, params.stream().map(sysRoleAuthConverter::toEntity).toList());
    }
}
