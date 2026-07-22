package com.fongtaoframework.starter.admin.modules.rights.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.rights.domain.entity.SysOrg;
import com.fongtaoframework.starter.admin.modules.rights.mapper.SysOrgMapper;
import com.fongtaoframework.starter.admin.modules.rights.service.ISysOrgService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysOrgService implements ISysOrgService {

    private final SysOrgMapper sysOrgMapper;

    @Override
    public PageResult<SysOrg> page(PageQuery pageQuery) {
        Page<SysOrg> page = sysOrgMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysOrg>().orderByAsc(SysOrg::getSortNo).orderByAsc(SysOrg::getSysOrgCode));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<SysOrg> list() {
        return sysOrgMapper.selectList(new LambdaQueryWrapper<SysOrg>().orderByAsc(SysOrg::getSortNo)
                .orderByAsc(SysOrg::getSysOrgCode));
    }

    @Override
    public SysOrg findById(String sysOrgId) {
        return sysOrgMapper.selectById(sysOrgId);
    }

    @Override
    public boolean existsByCode(String sysOrgCode, String excludedSysOrgId) {
        return sysOrgMapper.selectCount(new LambdaQueryWrapper<SysOrg>().eq(SysOrg::getSysOrgCode, sysOrgCode)
                .ne(excludedSysOrgId != null, SysOrg::getSysOrgId, excludedSysOrgId)) > 0;
    }

    @Override
    public boolean existsByParentId(String parentId) {
        return sysOrgMapper.selectCount(new LambdaQueryWrapper<SysOrg>().eq(SysOrg::getParentId, parentId)) > 0;
    }

    @Override
    public boolean save(SysOrg entity) {
        return sysOrgMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysOrg entity) {
        return sysOrgMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysOrgId) {
        return sysOrgMapper.deleteById(sysOrgId) == 1;
    }
}
