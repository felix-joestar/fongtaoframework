package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRightsExtra;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsExtraMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysRightsService implements ISysRightsService {

    private final SysRightsMapper sysRightsMapper;
    private final SysRightsExtraMapper sysRightsExtraMapper;

    @Override
    public PageResult<SysRights> page(PageQuery pageQuery) {
        Page<SysRights> page = sysRightsMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysRights>().orderByDesc(SysRights::getDefaulted)
                        .orderByDesc(SysRights::getCreateTime));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysRights findById(String sysRightsId) {
        return sysRightsMapper.selectById(sysRightsId);
    }

    @Override
    public SysRights findDefaultByUserId(String sysUserId) {
        return sysRightsMapper.selectOne(new LambdaQueryWrapper<SysRights>().eq(SysRights::getSysUserId, sysUserId)
                .eq(SysRights::getDefaulted, 1).eq(SysRights::getEnabled, 1).last("limit 1"));
    }

    @Override
    public List<String> listCustomOrgIds(String sysRightsId) {
        return sysRightsExtraMapper.selectList(new LambdaQueryWrapper<SysRightsExtra>()
                .eq(SysRightsExtra::getSysRightsId, sysRightsId)).stream().map(SysRightsExtra::getSysOrgId).toList();
    }

    @Override
    public boolean existsBySysUserId(String sysUserId) {
        return sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>()
                .eq(SysRights::getSysUserId, sysUserId)) > 0;
    }

    @Override
    public boolean existsBySysOrgId(String sysOrgId) {
        return sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>()
                .eq(SysRights::getSysOrgId, sysOrgId)) > 0;
    }

    @Override
    public boolean existsExtraBySysOrgId(String sysOrgId) {
        return sysRightsExtraMapper.selectCount(new LambdaQueryWrapper<SysRightsExtra>()
                .eq(SysRightsExtra::getSysOrgId, sysOrgId)) > 0;
    }

    @Override
    public boolean existsBySysRoleId(String sysRoleId) {
        return sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>()
                .eq(SysRights::getSysRoleId, sysRoleId)) > 0;
    }

    @Override
    public boolean existsByUserOrgRole(String sysUserId, String sysOrgId, String sysRoleId, String excludedSysRightsId) {
        return sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>().eq(SysRights::getSysUserId, sysUserId)
                .eq(SysRights::getSysOrgId, sysOrgId).eq(SysRights::getSysRoleId, sysRoleId)
                .ne(excludedSysRightsId != null, SysRights::getSysRightsId, excludedSysRightsId)) > 0;
    }

    @Override
    public void clearDefaulted(String sysUserId, String excludedSysRightsId) {
        sysRightsMapper.update(null, new LambdaUpdateWrapper<SysRights>().eq(SysRights::getSysUserId, sysUserId)
                .eq(SysRights::getDefaulted, 1).ne(excludedSysRightsId != null, SysRights::getSysRightsId, excludedSysRightsId)
                .set(SysRights::getDefaulted, 0));
    }

    @Override
    public boolean save(SysRights entity) {
        return sysRightsMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysRights entity) {
        return sysRightsMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysRightsId) {
        sysRightsExtraMapper.delete(new LambdaQueryWrapper<SysRightsExtra>().eq(SysRightsExtra::getSysRightsId, sysRightsId));
        return sysRightsMapper.deleteById(sysRightsId) == 1;
    }

    @Override
    public boolean replaceCustomSysOrgs(String sysRightsId, List<String> sysOrgIds) {
        sysRightsExtraMapper.delete(new LambdaQueryWrapper<SysRightsExtra>().eq(SysRightsExtra::getSysRightsId, sysRightsId));
        for (String sysOrgId : new LinkedHashSet<>(sysOrgIds == null ? List.of() : sysOrgIds)) {
            SysRightsExtra extra = new SysRightsExtra();
            extra.setSysRightsId(sysRightsId);
            extra.setSysOrgId(sysOrgId);
            if (sysRightsExtraMapper.insert(extra) != 1) {
                return false;
            }
        }
        return true;
    }
}
