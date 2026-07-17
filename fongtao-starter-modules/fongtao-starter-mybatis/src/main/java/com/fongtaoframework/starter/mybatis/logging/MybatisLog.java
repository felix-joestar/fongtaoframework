package com.fongtaoframework.starter.mybatis.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;

@Slf4j
public class MybatisLog implements Log {

    public MybatisLog(String clazz) {
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void error(String message, Throwable throwable) {
        log.error(message, throwable);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void trace(String message) {
        log.trace(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }
}
