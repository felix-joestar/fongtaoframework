package com.fongtaoframework.starter.admin.modules.basedata.service.impl;

import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDict;
import com.fongtaoframework.starter.admin.modules.basedata.domain.entity.SysDictItem;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictItemMapper;
import com.fongtaoframework.starter.admin.modules.basedata.mapper.SysDictMapper;
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

class SysDictItemServiceTest {

    private final SysDictItemMapper sysDictItemMapper = mock(SysDictItemMapper.class);
    private final SysDictMapper sysDictMapper = mock(SysDictMapper.class);
    private final CacheManager cacheManager = mock(CacheManager.class);
    private final Cache cache = mock(Cache.class);
    private final SysDictItemService service = new SysDictItemService(
            sysDictItemMapper, sysDictMapper, cacheManager);

    @BeforeEach
    void setUp() {
        TransactionSynchronizationManager.initSynchronization();
        when(cacheManager.getCache("fongtao:admin:dictionary-items")).thenReturn(cache);
        when(sysDictMapper.selectById("dict-1")).thenReturn(new SysDict());
        when(sysDictItemMapper.selectCount(any())).thenReturn(0L);
        when(sysDictItemMapper.insert(any(SysDictItem.class))).thenReturn(1);
    }

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void shouldClearCacheOnlyAfterCommit() {
        service.create(dictionaryItem());

        verify(cache, never()).clear();
        synchronizations().forEach(TransactionSynchronization::afterCommit);
        verify(cache).clear();
    }

    @Test
    void shouldKeepCacheWhenTransactionRollsBack() {
        service.create(dictionaryItem());

        synchronizations().forEach(
                synchronization -> synchronization.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK));

        verify(cache, never()).clear();
    }

    private List<TransactionSynchronization> synchronizations() {
        return TransactionSynchronizationManager.getSynchronizations();
    }

    private SysDictItem dictionaryItem() {
        SysDictItem item = new SysDictItem();
        item.setSysDictId("dict-1");
        item.setDictItemValue("value");
        return item;
    }
}
