package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleAuthMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysResService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleAuthService;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleService;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysRoleAuthService implements ISysRoleAuthService {

    private final SysRoleAuthMapper sysRoleAuthMapper;
    private final ISysRoleService sysRoleService;
    private final ISysResService sysResService;

    @Override
    public List<SysRes> listAuthResources(String sysRoleId) {
        sysRoleService.get(sysRoleId);
        return sysResService.listByRoleId(sysRoleId);
    }

    @Override
    @Transactional
    public void replaceAuthResources(String sysRoleId, List<SysRoleAuth> sysRoleAuths) {
        sysRoleService.get(sysRoleId);
        List<String> distinctIds = sysRoleAuths == null ? List.of() : new java.util.ArrayList<>(new LinkedHashSet<>(
                sysRoleAuths.stream().map(SysRoleAuth::getSysResId).toList()));
        for (String sysResId : distinctIds) {
            if (sysResId == null || sysResId.isBlank()) throw new BusinessException("资源标识不能为空");
            sysResService.get(sysResId);
        }
        sysRoleAuthMapper.delete(new LambdaQueryWrapper<SysRoleAuth>().eq(SysRoleAuth::getSysRoleId, sysRoleId));
        for (String sysResId : distinctIds) {
            SysRoleAuth entity = new SysRoleAuth();
            entity.setSysRoleAuthId(IdUtil.simpleUUID());
            entity.setSysRoleId(sysRoleId);
            entity.setSysResId(sysResId);
            if (sysRoleAuthMapper.insert(entity) != 1) throw new BusinessException("角色资源授权失败");
        }
    }
}
