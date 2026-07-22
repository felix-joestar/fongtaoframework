package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictItemMapper;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SysDictItemService implements ISysDictItemService {

    private static final int ENABLED = 1;

    private final SysDictItemMapper sysDictItemMapper;

    @Override
    public PageResult<SysDictItem> page(PageQuery pageQuery) {
        Page<SysDictItem> page = sysDictItemMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysDictItem>().orderByAsc(SysDictItem::getSortNo)
                        .orderByAsc(SysDictItem::getDictItemValue));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysDictItem findById(String sysDictItemId) {
        return sysDictItemMapper.selectById(sysDictItemId);
    }

    @Override
    public boolean existsByDictId(String sysDictId) {
        return sysDictItemMapper.selectCount(new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getSysDictId, sysDictId)) > 0;
    }

    @Override
    public boolean existsByDictIdAndValue(String sysDictId, String dictItemValue, String excludedSysDictItemId) {
        return sysDictItemMapper.selectCount(new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getSysDictId, sysDictId).eq(SysDictItem::getDictItemValue, dictItemValue)
                .ne(excludedSysDictItemId != null, SysDictItem::getSysDictItemId, excludedSysDictItemId)) > 0;
    }

    @Override
    public List<SysDictItem> listEnabledByDictId(String sysDictId) {
        return sysDictItemMapper.selectList(new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getSysDictId, sysDictId).eq(SysDictItem::getEnabled, ENABLED)
                .orderByAsc(SysDictItem::getSortNo).orderByAsc(SysDictItem::getDictItemValue));
    }

    @Override
    public boolean save(SysDictItem entity) {
        return sysDictItemMapper.insert(entity) == 1;
    }

    @Override
    public boolean updateById(SysDictItem entity) {
        return sysDictItemMapper.updateById(entity) == 1;
    }

    @Override
    public boolean deleteById(String sysDictItemId) {
        return sysDictItemMapper.deleteById(sysDictItemId) == 1;
    }
}
