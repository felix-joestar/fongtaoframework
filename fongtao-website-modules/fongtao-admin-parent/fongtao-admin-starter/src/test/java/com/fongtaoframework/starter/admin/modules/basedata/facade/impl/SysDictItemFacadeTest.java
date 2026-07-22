package com.fongtaoframework.starter.admin.modules.basedata.facade.impl;

import com.fongtaoframework.starter.admin.modules.basedata.converter.SysDictItemConverter;
import com.fongtaoframework.starter.admin.modules.basedata.domain.dto.param.SysDictItemCreateParam;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictItemService;
import com.fongtaoframework.starter.admin.modules.basedata.service.ISysDictService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysDictItemFacadeTest {

    private final ISysDictItemService sysDictItemService = mock(ISysDictItemService.class);
    private final ISysDictService sysDictService = mock(ISysDictService.class);
    private final SysDictItemConverter sysDictItemConverter = mock(SysDictItemConverter.class);
    private final CacheManager cacheManager = mock(CacheManager.class);
    private final Cache cache = mock(Cache.class);
    private final SysDictItemFacade facade = new SysDictItemFacade(
            sysDictItemService, sysDictService, sysDictItemConverter, cacheManager);

    @BeforeEach
    void setUp() {
        TransactionSynchronizationManager.initSynchronization();
        when(cacheManager.getCache("fongtao:admin:dictionary-items")).thenReturn(cache);
        when(sysDictService.findById("dict-1")).thenReturn(new SysDict());
        when(sysDictItemService.existsByDictIdAndValue(any(), any(), any())).thenReturn(false);
        when(sysDictItemService.save(any(SysDictItem.class))).thenReturn(true);
        when(sysDictItemConverter.toEntity(any(SysDictItemCreateParam.class))).thenReturn(dictionaryItem());
    }

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void shouldClearCacheOnlyAfterCommit() {
        facade.create(createParam());

        verify(cache, never()).clear();
        synchronizations().forEach(TransactionSynchronization::afterCommit);
        verify(cache).clear();
    }

    @Test
    void shouldKeepCacheWhenTransactionRollsBack() {
        facade.create(createParam());

        synchronizations().forEach(synchronization ->
                synchronization.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK));

        verify(cache, never()).clear();
    }

    private List<TransactionSynchronization> synchronizations() {
        return TransactionSynchronizationManager.getSynchronizations();
    }

    private SysDictItemCreateParam createParam() {
        return new SysDictItemCreateParam("dict-1", "value", "标签", 1, 1, null);
    }

    private SysDictItem dictionaryItem() {
        SysDictItem item = new SysDictItem();
        item.setSysDictId("dict-1");
        item.setDictItemValue("value");
        return item;
    }
}
