package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysRes;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysResMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysResService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysResService implements ISysResService {

    private final SysResMapper sysResMapper;

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
    public List<SysRes> listByIds(List<String> sysResIds) {
        if (sysResIds == null || sysResIds.isEmpty()) {
            return List.of();
        }
        return sysResMapper.selectList(new LambdaQueryWrapper<SysRes>().in(SysRes::getSysResId, sysResIds)
                .orderByAsc(SysRes::getSortNo).orderByAsc(SysRes::getSysResCode));
    }

    @Override
    public SysRes findById(String sysResId) {
        return sysResMapper.selectById(sysResId);
    }

    @Override
    public boolean existsByCode(String sysResCode, String excludedSysResId) {
        return sysResMapper.selectCount(new LambdaQueryWrapper<SysRes>().eq(SysRes::getSysResCode, sysResCode)
                .ne(excludedSysResId != null, SysRes::getSysResId, excludedSysResId)) > 0;
    }

    @Override
    public boolean existsByParentId(String parentId) {
        return sysResMapper.selectCount(new LambdaQueryWrapper<SysRes>().eq(SysRes::getParentId, parentId)) > 0;
    }

    @Override
    public boolean save(SysRes entity) {
        return sysResMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysRes entity) {
        return sysResMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysResId) {
        return sysResMapper.deleteById(sysResId) == 1;
    }
}
