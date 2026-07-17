package com.fongtaoframework.core;

public record PageQuery(long pageNo, long pageSize) {

    public static final long DEFAULT_PAGE_NO = 1;
    public static final long DEFAULT_PAGE_SIZE = 10;
    public static final long MAX_PAGE_SIZE = 500;

    public PageQuery {
        pageNo = normalizePageNo(pageNo);
        pageSize = normalizePageSize(pageSize);
    }

    public static PageQuery of(Long pageNo, Long pageSize) {
        long normalizedPageNo = pageNo == null ? DEFAULT_PAGE_NO : pageNo;
        long normalizedPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        return new PageQuery(normalizedPageNo, normalizedPageSize);
    }

    public long offset() {
        return (pageNo - 1) * pageSize;
    }

    private static long normalizePageNo(long pageNo) {
        return pageNo < 1 ? DEFAULT_PAGE_NO : pageNo;
    }

    private static long normalizePageSize(long pageSize) {
        if (pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }
}
