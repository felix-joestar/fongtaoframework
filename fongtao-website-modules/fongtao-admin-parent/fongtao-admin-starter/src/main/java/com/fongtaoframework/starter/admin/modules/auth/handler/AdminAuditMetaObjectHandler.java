package com.fongtaoframework.starter.admin.modules.auth.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fongtaoframework.starter.security.util.SecurityUtil;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class AdminAuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        String userId = SecurityUtil.getCurrentUser().map(loginUser -> loginUser.getUserId()).orElse(null);
        strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        if (userId != null) {
            strictInsertFill(metaObject, "createId", String.class, userId);
            strictInsertFill(metaObject, "updateId", String.class, userId);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        SecurityUtil.getCurrentUser().ifPresent(loginUser ->
                strictUpdateFill(metaObject, "updateId", String.class, loginUser.getUserId()));
    }
}
