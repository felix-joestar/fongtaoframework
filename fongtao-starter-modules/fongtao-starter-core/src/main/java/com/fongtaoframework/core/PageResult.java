package com.fongtaoframework.core;

import java.util.List;

public record PageResult<T>(List<T> records, long total, long pageNo, long pageSize, long pages) {

    public static <T> PageResult<T> of(List<T> records, long total, long pageNo, long pageSize) {
        long pages = pageSize <= 0 ? 0 : (total + pageSize - 1) / pageSize;
        return new PageResult<>(List.copyOf(records), total, pageNo, pageSize, pages);
    }
}
