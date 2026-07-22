package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleAuthMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRoleAuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysRoleAuthService implements ISysRoleAuthService {

    private final SysRoleAuthMapper sysRoleAuthMapper;

    @Override
    public List<String> listResourceIdsByRoleId(String sysRoleId) {
        return sysRoleAuthMapper.selectList(new LambdaQueryWrapper<SysRoleAuth>()
                .eq(SysRoleAuth::getSysRoleId, sysRoleId)).stream().map(SysRoleAuth::getSysResId).toList();
    }

    @Override
    public List<SysRes> listEffectiveResourcesByUserId(String sysUserId) {
        return sysRoleAuthMapper.selectDefaultVisibleResourcesByUserId(sysUserId);
    }

    @Override
    public List<String> listEffectivePermissionCodesByUserId(String sysUserId) {
        return sysRoleAuthMapper.selectDefaultPermissionsByUserId(sysUserId);
    }

    @Override
    public boolean existsByRoleId(String sysRoleId) {
        return sysRoleAuthMapper.selectCount(new LambdaQueryWrapper<SysRoleAuth>()
                .eq(SysRoleAuth::getSysRoleId, sysRoleId)) > 0;
    }

    @Override
    public boolean existsByResId(String sysResId) {
        return sysRoleAuthMapper.selectCount(new LambdaQueryWrapper<SysRoleAuth>()
                .eq(SysRoleAuth::getSysResId, sysResId)) > 0;
    }

    @Override
    public boolean replaceByRoleId(String sysRoleId, List<SysRoleAuth> sysRoleAuths) {
        sysRoleAuthMapper.delete(new LambdaQueryWrapper<SysRoleAuth>().eq(SysRoleAuth::getSysRoleId, sysRoleId));
        for (SysRoleAuth sysRoleAuth : sysRoleAuths) {
            if (sysRoleAuthMapper.insert(sysRoleAuth) != 1) {
                return false;
            }
        }
        return true;
    }
}
