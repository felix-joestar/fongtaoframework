package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.common.enums.DataScope;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRights;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRightsExtra;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRole;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysUser;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysOrgMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsExtraMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRightsMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysUserMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysRightsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysRightsService implements ISysRightsService {

    private final SysRightsMapper sysRightsMapper;
    private final SysRightsExtraMapper sysRightsExtraMapper;
    private final SysUserMapper sysUserMapper;
    private final SysOrgMapper sysOrgMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    public PageResult<SysRights> page(PageQuery pageQuery) {
        Page<SysRights> page = sysRightsMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysRights>().orderByDesc(SysRights::getDefaulted)
                        .orderByDesc(SysRights::getCreateTime));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysRights get(String sysRightsId) {
        SysRights entity = sysRightsMapper.selectById(sysRightsId);
        if (entity == null) {
            throw new BusinessException("身份不存在或已删除");
        }
        return entity;
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
    @Transactional
    public void create(SysRights entity, List<String> customSysOrgIds) {
        entity.setSysRightsId(IdUtil.simpleUUID());
        assertValid(entity, customSysOrgIds, null);
        if (sysRightsMapper.insert(entity) != 1) {
            throw new BusinessException("身份新增失败");
        }
        replaceCustomSysOrgs(entity.getSysRightsId(), customSysOrgIds);
    }

    @Override
    @Transactional
    public void updateById(SysRights entity, List<String> customSysOrgIds) {
        get(entity.getSysRightsId());
        assertValid(entity, customSysOrgIds, entity.getSysRightsId());
        if (sysRightsMapper.updateById(entity) != 1) {
            throw new BusinessException("身份更新失败");
        }
        replaceCustomSysOrgs(entity.getSysRightsId(), customSysOrgIds);
    }

    @Override
    @Transactional
    public void updateEnabled(String sysRightsId, Integer enabled) {
        SysRights entity = get(sysRightsId);
        entity.setEnabled(enabled);
        if (sysRightsMapper.updateById(entity) != 1) {
            throw new BusinessException("身份状态更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysRightsId) {
        get(sysRightsId);
        sysRightsExtraMapper.delete(new LambdaQueryWrapper<SysRightsExtra>().eq(SysRightsExtra::getSysRightsId, sysRightsId));
        if (sysRightsMapper.deleteById(sysRightsId) != 1) {
            throw new BusinessException("身份删除失败");
        }
    }

    private void assertValid(SysRights entity, List<String> customSysOrgIds, String currentId) {
        if (sysUserMapper.selectById(entity.getSysUserId()) == null || sysOrgMapper.selectById(entity.getSysOrgId()) == null
                || sysRoleMapper.selectById(entity.getSysRoleId()) == null) {
            throw new BusinessException("用户、组织或角色不存在或已删除");
        }
        if (!DataScope.supports(entity.getDataScope())) {
            throw new BusinessException("数据范围不合法");
        }
        if (sysRightsMapper.selectCount(new LambdaQueryWrapper<SysRights>().eq(SysRights::getSysUserId, entity.getSysUserId())
                .eq(SysRights::getSysOrgId, entity.getSysOrgId()).eq(SysRights::getSysRoleId, entity.getSysRoleId())
                .ne(StrUtil.isNotBlank(currentId), SysRights::getSysRightsId, currentId)) > 0) {
            throw new BusinessException("用户组织角色身份已存在");
        }
        if (Integer.valueOf(1).equals(entity.getDefaulted())) {
            sysRightsMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<SysRights>()
                    .eq(SysRights::getSysUserId, entity.getSysUserId()).eq(SysRights::getDefaulted, 1)
                    .ne(StrUtil.isNotBlank(currentId), SysRights::getSysRightsId, currentId).set(SysRights::getDefaulted, 0));
        }
        if (DataScope.CUSTOM.getCode().equals(entity.getDataScope()) && CollUtil.isEmpty(customSysOrgIds)) {
            throw new BusinessException("自定义数据范围必须指定组织");
        }
    }

    private void replaceCustomSysOrgs(String sysRightsId, List<String> customSysOrgIds) {
        sysRightsExtraMapper.delete(new LambdaQueryWrapper<SysRightsExtra>().eq(SysRightsExtra::getSysRightsId, sysRightsId));
        if (CollUtil.isEmpty(customSysOrgIds)) {
            return;
        }
        for (String sysOrgId : CollUtil.distinct(customSysOrgIds)) {
            if (sysOrgMapper.selectById(sysOrgId) == null) {
                throw new BusinessException("自定义数据范围组织不存在或已删除");
            }
            SysRightsExtra extra = new SysRightsExtra();
            extra.setSysRightsExtraId(IdUtil.simpleUUID());
            extra.setSysRightsId(sysRightsId);
            extra.setSysOrgId(sysOrgId);
            sysRightsExtraMapper.insert(extra);
        }
    }
}
