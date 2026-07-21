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
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SysDictService implements ISysDictService {

    private static final int ENABLED = 1;
    private static final String ITEM_CACHE = "fongtao:admin:dictionary-items";

    private final SysDictMapper sysDictMapper;
    private final SysDictItemMapper sysDictItemMapper;
    private final CacheManager cacheManager;

    @Override
    public PageResult<SysDict> page(PageQuery pageQuery) {
        Page<SysDict> page = sysDictMapper.selectPage(new Page<>(pageQuery.pageNo(), pageQuery.pageSize()),
                new LambdaQueryWrapper<SysDict>().orderByDesc(SysDict::getCreateTime));
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysDict get(String sysDictId) {
        SysDict entity = sysDictMapper.selectById(sysDictId);
        if (entity == null) {
            throw new BusinessException("字典不存在或已删除");
        }
        return entity;
    }

    @Override
    @Transactional
    public void create(SysDict entity) {
        assertCodeUnique(entity.getSysDictCode(), null);
        if (sysDictMapper.insert(entity) != 1) {
            throw new BusinessException("字典新增失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @Transactional
    public void updateById(SysDict entity) {
        get(entity.getSysDictId());
        assertCodeUnique(entity.getSysDictCode(), entity.getSysDictId());
        if (sysDictMapper.updateById(entity) != 1) {
            throw new BusinessException("字典更新失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @Transactional
    public void deleteById(String sysDictId) {
        get(sysDictId);
        if (sysDictItemMapper.selectCount(new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getSysDictId, sysDictId)) > 0) {
            throw new BusinessException("字典仍包含字典项，不能删除");
        }
        if (sysDictMapper.deleteById(sysDictId) != 1) {
            throw new BusinessException("字典删除失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysDictItem> listEnabledItems(String sysDictCode) {
        Cache cache = cacheManager.getCache(ITEM_CACHE);
        List<SysDictItem> cached = cache == null ? null : cache.get(sysDictCode, List.class);
        if (cached != null) {
            return cached;
        }
        SysDict dictionary = sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getSysDictCode, sysDictCode)
                .eq(SysDict::getEnabled, ENABLED)
                .last("limit 1"));
        List<SysDictItem> items = dictionary == null ? List.of() : sysDictItemMapper.selectList(
                new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getSysDictId, dictionary.getSysDictId())
                        .eq(SysDictItem::getEnabled, ENABLED).orderByAsc(SysDictItem::getSortNo)
                        .orderByAsc(SysDictItem::getDictItemValue));
        List<SysDictItem> result = List.copyOf(items);
        if (cache != null) {
            cache.put(sysDictCode, result);
        }
        return result;
    }

    private void assertCodeUnique(String code, String currentId) {
        if (sysDictMapper.selectCount(new LambdaQueryWrapper<SysDict>().eq(SysDict::getSysDictCode, code)
                .ne(StrUtil.isNotBlank(currentId), SysDict::getSysDictId, currentId)) > 0) {
            throw new BusinessException("字典编码已存在");
        }
    }

    private void clearItemCacheAfterCommit() {
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
