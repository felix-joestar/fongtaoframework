package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictMapper;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysDictService implements ISysDictService {

    private static final int ENABLED = 1;

    private final SysDictMapper sysDictMapper;

    @Override
    public PageResult<SysDict> page(PageQuery pageQuery) {
        Page<SysDict> page = sysDictMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysDict>().orderByDesc(SysDict::getCreateTime));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysDict findById(String sysDictId) {
        return sysDictMapper.selectById(sysDictId);
    }

    @Override
    public SysDict findEnabledByCode(String sysDictCode) {
        return sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getSysDictCode, sysDictCode).eq(SysDict::getEnabled, ENABLED).last("limit 1"));
    }

    @Override
    public boolean existsByCode(String sysDictCode, String excludedSysDictId) {
        return sysDictMapper.selectCount(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getSysDictCode, sysDictCode)
                .ne(excludedSysDictId != null, SysDict::getSysDictId, excludedSysDictId)) > 0;
    }

    @Override
    public boolean save(SysDict entity) {
        return sysDictMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysDict entity) {
        return sysDictMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysDictId) {
        return sysDictMapper.deleteById(sysDictId) == 1;
    }
}
