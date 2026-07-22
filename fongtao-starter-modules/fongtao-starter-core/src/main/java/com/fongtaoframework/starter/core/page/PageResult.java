package com.fongtaoframework.starter.core.page;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record PageResult<T>(List<T> records, long total, long pageNo, long pageSize, long pages) {

    public PageResult {
        records = records == null ? List.of() : List.copyOf(records);
    }

    public static <T> PageResult<T> of(List<T> records, long total, long pageNo, long pageSize) {
        long pages = pageSize <= 0 ? 0 : (total + pageSize - 1) / pageSize;
        return new PageResult<>(records, total, pageNo, pageSize, pages);
    }

    public <U> PageResult<U> map(Function<? super T, U> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        return new PageResult<>(records.stream().map(mapper).toList(), total, pageNo, pageSize, pages);
    }
}
