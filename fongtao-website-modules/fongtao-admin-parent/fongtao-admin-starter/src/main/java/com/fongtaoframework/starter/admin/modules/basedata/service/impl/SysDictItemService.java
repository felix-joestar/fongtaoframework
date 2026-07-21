package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fongtaoframework.core.BusinessException;
import com.fongtaoframework.core.PageQuery;
import com.fongtaoframework.core.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictItemMapper;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictMapper;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysDictItemService implements ISysDictItemService {

    private static final String ITEM_CACHE = "fongtao:admin:dictionary-items";

    private final SysDictItemMapper sysDictItemMapper;
    private final SysDictMapper sysDictMapper;
    private final CacheManager cacheManager;

    @Override
    public PageResult<SysDictItem> page(PageQuery pageQuery) {
        Page<SysDictItem> page = sysDictItemMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysDictItem>().orderByAsc(SysDictItem::getSortNo)
                        .orderByAsc(SysDictItem::getDictItemValue));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysDictItem get(String sysDictItemId) {
        SysDictItem entity = sysDictItemMapper.selectById(sysDictItemId);
        if (entity == null) {
            throw new BusinessException("字典项不存在或已删除");
        }
        return entity;
    }

    @Override
    @Transactional
    public void create(SysDictItem entity) {
        assertDictionaryExists(entity.getSysDictId());
        assertValueUnique(entity.getSysDictId(), entity.getDictItemValue(), null);
        if (sysDictItemMapper.insert(entity) != 1) {
            throw new BusinessException("字典项新增失败");
        }
        clearCacheAfterCommit();
    }

    @Override
    @Transactional
    public void updateById(SysDictItem entity) {
        get(entity.getSysDictItemId());
        assertDictionaryExists(entity.getSysDictId());
        assertValueUnique(entity.getSysDictId(), entity.getDictItemValue(), entity.getSysDictItemId());
        if (sysDictItemMapper.updateById(entity) != 1) {
            throw new BusinessException("字典项更新失败");
        }
        clearCacheAfterCommit();
    }

    @Override
    @Transactional
    public void deleteById(String sysDictItemId) {
        get(sysDictItemId);
        if (sysDictItemMapper.deleteById(sysDictItemId) != 1) {
            throw new BusinessException("字典项删除失败");
        }
        clearCacheAfterCommit();
    }

    private void assertDictionaryExists(String sysDictId) {
        if (sysDictMapper.selectById(sysDictId) == null) {
            throw new BusinessException("所属字典不存在或已删除");
        }
    }

    private void assertValueUnique(String sysDictId, String value, String currentId) {
        if (sysDictItemMapper.selectCount(new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getSysDictId, sysDictId).eq(SysDictItem::getDictItemValue, value)
                .ne(StrUtil.isNotBlank(currentId), SysDictItem::getSysDictItemId, currentId)) > 0) {
            throw new BusinessException("字典项值已存在");
        }
    }

    private void clearCacheAfterCommit() {
        Cache cache = cacheManager.getCache(ITEM_CACHE);
        if (cache == null) {
            return;
        }
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            cache.clear();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                cache.clear();
            }
        });
    }
}
