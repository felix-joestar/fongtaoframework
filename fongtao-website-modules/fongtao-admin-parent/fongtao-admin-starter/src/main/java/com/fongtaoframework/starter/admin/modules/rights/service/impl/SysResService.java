package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.common.enums.ResourceType;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRoleAuth;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysResMapper;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysRoleAuthMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysResService;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysResService implements ISysResService {

    private final SysResMapper sysResMapper;
    private final SysRoleAuthMapper sysRoleAuthMapper;

    @Override
    public PageResult<SysRes> page(PageQuery pageQuery) {
        Page<SysRes> page = sysResMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysRes>().orderByAsc(SysRes::getSortNo).orderByAsc(SysRes::getSysResCode));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<SysRes> list() {
        return sysResMapper.selectList(new LambdaQueryWrapper<SysRes>().orderByAsc(SysRes::getSortNo)
                .orderByAsc(SysRes::getSysResCode));
    }

    @Override
    public List<SysRes> listByRoleId(String sysRoleId) {
        List<String> resourceIds = sysRoleAuthMapper.selectList(new LambdaQueryWrapper<SysRoleAuth>()
                        .eq(SysRoleAuth::getSysRoleId, sysRoleId))
                .stream().map(SysRoleAuth::getSysResId).toList();
        if (resourceIds.isEmpty()) {
            return List.of();
        }
        return sysResMapper.selectList(new LambdaQueryWrapper<SysRes>().in(SysRes::getSysResId, resourceIds)
                .orderByAsc(SysRes::getSortNo).orderByAsc(SysRes::getSysResCode));
    }

    @Override
    public List<String> listEnabledPermissionCodesByUserId(String sysUserId) {
        return sysRoleAuthMapper.selectDefaultPermissionsByUserId(sysUserId);
    }

    @Override
    public List<SysRes> listVisibleByUserId(String sysUserId) {
        return sysRoleAuthMapper.selectDefaultVisibleResourcesByUserId(sysUserId);
    }

    @Override
    public SysRes get(String sysResId) {
        SysRes entity = sysResMapper.selectById(sysResId);
        if (entity == null) {
            throw new BusinessException("资源不存在或已删除");
        }
        return entity;
    }

    @Override
    @Transactional
    public void create(SysRes entity) {
        assertParent(entity.getSysResId(), entity.getParentId());
        assertCodeUnique(entity.getSysResCode(), null);
        assertType(entity.getSysResType());
        if (sysResMapper.insert(entity) != 1) {
            throw new BusinessException("资源新增失败");
        }
    }

    @Override
    @Transactional
    public void updateById(SysRes entity) {
        get(entity.getSysResId());
        assertParent(entity.getSysResId(), entity.getParentId());
        assertCodeUnique(entity.getSysResCode(), entity.getSysResId());
        assertType(entity.getSysResType());
        if (sysResMapper.updateById(entity) != 1) {
            throw new BusinessException("资源更新失败");
        }
    }

    @Override
    @Transactional
    public void deleteById(String sysResId) {
        get(sysResId);
        if (sysResMapper.selectCount(new LambdaQueryWrapper<SysRes>().eq(SysRes::getParentId, sysResId)) > 0) {
            throw new BusinessException("资源存在下级，不能删除");
        }
        if (sysRoleAuthMapper.selectCount(new LambdaQueryWrapper<SysRoleAuth>()
                .eq(SysRoleAuth::getSysResId, sysResId)) > 0) {
            throw new BusinessException("资源仍被角色授权引用，不能删除");
        }
        if (sysResMapper.deleteById(sysResId) != 1) {
            throw new BusinessException("资源删除失败");
        }
    }

    private void assertParent(String currentId, String parentId) {
        if (StrUtil.isBlank(parentId)) {
            return;
        }
        if (StrUtil.equals(currentId, parentId)) {
            throw new BusinessException("资源不能设置自身为上级");
        }
        Set<String> visited = new LinkedHashSet<>();
        String current = parentId;
        while (StrUtil.isNotBlank(current)) {
            if (!visited.add(current) || StrUtil.equals(currentId, current)) {
                throw new BusinessException("资源层级存在循环");
            }
            current = get(current).getParentId();
        }
    }

    private void assertCodeUnique(String code, String currentId) {
        if (sysResMapper.selectCount(new LambdaQueryWrapper<SysRes>().eq(SysRes::getSysResCode, code)
                .ne(StrUtil.isNotBlank(currentId), SysRes::getSysResId, currentId)) > 0) {
            throw new BusinessException("资源编码已存在");
        }
    }

    private void assertType(String type) {
        if (!ResourceType.supports(type)) {
            throw new BusinessException("资源类型不支持");
        }
    }
}
