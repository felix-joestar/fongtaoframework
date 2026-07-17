package com.fongtaoframework.starter.mybatis.logging;

import cn.hutool.core.util.StrUtil;
import com.fongtaoframework.core.TraceIdContext;
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6spySqlFormat implements MessageFormattingStrategy {

    @Override
    public String formatMessage(
            int connectionId,
            String now,
            long elapsed,
            String category,
            String prepared,
            String sql,
            String url) {
        if (StrUtil.isBlank(sql)) {
            return StrUtil.EMPTY;
        }
        return "traceId=" + TraceIdContext.currentOrCreate()
                + "|connectionId=" + connectionId
                + "|category=" + category
                + "|elapsedMs=" + elapsed
                + "|sql=" + P6Util.singleLine(sql);
    }
}
