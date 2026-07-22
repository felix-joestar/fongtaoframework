package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysDictItemConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysDictItemFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictItemService;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RequiredArgsConstructor
@Component
public class SysDictItemFacade implements ISysDictItemFacade {

    private static final String ITEM_CACHE = "fongtao:admin:dictionary-items";

    private final ISysDictItemService sysDictItemService;
    private final ISysDictService sysDictService;
    private final SysDictItemConverter sysDictItemConverter;
    private final CacheManager cacheManager;

    @Override
    public PageResult<SysDictItemRow> page(SysDictItemPageParam param) {
        SysDictItemPageParam pageParam = param == null ? new SysDictItemPageParam(null, null) : param;
        PageResult<SysDictItem> page = sysDictItemService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysDictItemConverter::toRow);
    }

    @Override
    public SysDictItemRow getById(String sysDictItemId) {
        return sysDictItemConverter.toRow(require(sysDictItemId));
    }

    @Override
    @Transactional
    public void create(SysDictItemCreateParam param) {
        SysDictItem entity = sysDictItemConverter.toEntity(param);
        assertDictionaryExists(entity.getSysDictId());
        assertValueUnique(entity.getSysDictId(), entity.getDictItemValue(), null);
        if (!sysDictItemService.save(entity)) {
            throw new BusinessException("字典项新增失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @Transactional
    public void updateById(SysDictItemUpdateParam param) {
        SysDictItem entity = sysDictItemConverter.toEntity(param);
        require(entity.getSysDictItemId());
        assertDictionaryExists(entity.getSysDictId());
        assertValueUnique(entity.getSysDictId(), entity.getDictItemValue(), entity.getSysDictItemId());
        if (!sysDictItemService.updateById(entity)) {
            throw new BusinessException("字典项更新失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @Transactional
    public void deleteById(String sysDictItemId) {
        require(sysDictItemId);
        if (!sysDictItemService.deleteById(sysDictItemId)) {
            throw new BusinessException("字典项删除失败");
        }
        clearItemCacheAfterCommit();
    }

    private SysDictItem require(String sysDictItemId) {
        SysDictItem entity = sysDictItemService.findById(sysDictItemId);
        if (entity == null) {
            throw new BusinessException("字典项不存在或已删除");
        }
        return entity;
    }

    private void assertDictionaryExists(String sysDictId) {
        SysDict dictionary = sysDictService.findById(sysDictId);
        if (dictionary == null) {
            throw new BusinessException("所属字典不存在或已删除");
        }
    }

    private void assertValueUnique(String sysDictId, String dictItemValue, String currentId) {
        if (sysDictItemService.existsByDictIdAndValue(sysDictId, dictItemValue, currentId)) {
            throw new BusinessException("字典项值已存在");
        }
    }

    private void clearItemCacheAfterCommit() {
        Cache cache = cacheManager.getCache(ITEM_CACHE);
        if (cache == null) {
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
