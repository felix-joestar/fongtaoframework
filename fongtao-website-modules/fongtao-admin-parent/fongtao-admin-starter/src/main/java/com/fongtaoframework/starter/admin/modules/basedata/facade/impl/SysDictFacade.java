package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import com.fongtaoframework.starter.core.exception.BusinessException;
import com.fongtaoframework.starter.core.page.PageQuery;
import com.fongtaoframework.starter.core.page.PageResult;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysDictConverter;
import com.fongtaoframework.starter.admin.modules.basedata.converter.SysDictItemConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictItemRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.SysDictRow;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictPageParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictUpdateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.admin.modules.basedata.facade.ISysDictFacade;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictItemService;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RequiredArgsConstructor
@Component
public class SysDictFacade implements ISysDictFacade {

    private static final String ITEM_CACHE = "fongtao:admin:dictionary-items";

    private final ISysDictService sysDictService;
    private final ISysDictItemService sysDictItemService;
    private final SysDictConverter sysDictConverter;
    private final SysDictItemConverter sysDictItemConverter;
    private final CacheManager cacheManager;

    @Override
    public PageResult<SysDictRow> page(SysDictPageParam param) {
        SysDictPageParam pageParam = param == null ? new SysDictPageParam(null, null) : param;
        PageResult<SysDict> page = sysDictService.page(PageQuery.of(pageParam.pageNo(), pageParam.pageSize()));
        return page.map(sysDictConverter::toRow);
    }

    @Override
    public SysDictRow getById(String sysDictId) {
        return sysDictConverter.toRow(require(sysDictId));
    }

    @Override
    @Transactional
    public void create(SysDictCreateParam param) {
        SysDict entity = sysDictConverter.toEntity(param);
        assertCodeUnique(entity.getSysDictCode(), null);
        if (!sysDictService.save(entity)) {
            throw new BusinessException("字典新增失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @Transactional
    public void updateById(SysDictUpdateParam param) {
        SysDict entity = sysDictConverter.toEntity(param);
        require(entity.getSysDictId());
        assertCodeUnique(entity.getSysDictCode(), entity.getSysDictId());
        if (!sysDictService.updateById(entity)) {
            throw new BusinessException("字典更新失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @Transactional
    public void deleteById(String sysDictId) {
        require(sysDictId);
        if (sysDictItemService.existsByDictId(sysDictId)) {
            throw new BusinessException("字典仍包含字典项，不能删除");
        }
        if (!sysDictService.deleteById(sysDictId)) {
            throw new BusinessException("字典删除失败");
        }
        clearItemCacheAfterCommit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysDictItemRow> options(String sysDictCode) {
        Cache cache = cacheManager.getCache(ITEM_CACHE);
        List<SysDictItem> items = cache == null ? null : cache.get(sysDictCode, List.class);
        if (items == null) {
            SysDict dict = sysDictService.findEnabledByCode(sysDictCode);
            items = dict == null ? List.of() : List.copyOf(sysDictItemService.listEnabledByDictId(dict.getSysDictId()));
            if (cache != null) {
                cache.put(sysDictCode, items);
            }
        }
        return items.stream().map(sysDictItemConverter::toRow).toList();
    }

    private SysDict require(String sysDictId) {
        SysDict entity = sysDictService.findById(sysDictId);
        if (entity == null) {
            throw new BusinessException("字典不存在或已删除");
        }
        return entity;
    }

    private void assertCodeUnique(String code, String currentId) {
        if (sysDictService.existsByCode(code, currentId)) {
            throw new BusinessException("字典编码已存在");
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
