-- Fongtao admin MySQL baseline schema.
-- This file is a schema design baseline for admin rights and basedata modules.
-- Production changes should be released through reviewed migrations.

-- ---------------------------------------------------------------------------
-- rights module
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `sys_user`
(
    `sys_user_id`     varchar(32)  NOT NULL COMMENT '用户主键',
    `sys_user_code`   varchar(64)  NOT NULL COMMENT '用户账号',
    `sys_user_pwd`    varchar(128) NOT NULL COMMENT '用户密码',
    `sys_user_name`   varchar(64)  NOT NULL COMMENT '用户名称',
    `sys_user_mobile` varchar(32)  NULL DEFAULT NULL COMMENT '用户手机号',
    `sys_user_email`  varchar(128) NULL DEFAULT NULL COMMENT '用户邮箱',
    `avatar_file_id`  varchar(32)  NULL DEFAULT NULL COMMENT '头像文件主键',
    `sys_user_status` tinyint      NOT NULL DEFAULT 1 COMMENT '用户状态：1启用，0禁用',
    `sys_data`        json         NULL COMMENT '用户扩展数据',
    `create_id`       varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`       varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`         tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_user_id`) USING BTREE,
    UNIQUE KEY `uk_sys_user_code_deleted` (`sys_user_code`, `deleted`) USING BTREE,
    KEY `idx_sys_user_mobile_deleted` (`sys_user_mobile`, `deleted`) USING BTREE,
    KEY `idx_sys_user_deleted_create_time` (`deleted`, `create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统用户';

CREATE TABLE IF NOT EXISTS `sys_org`
(
    `sys_org_id`   varchar(32)  NOT NULL COMMENT '组织主键',
    `parent_id`    varchar(32)  NULL DEFAULT NULL COMMENT '上级组织主键',
    `sys_org_code` varchar(64)  NOT NULL COMMENT '组织编码',
    `sys_org_name` varchar(128) NOT NULL COMMENT '组织名称',
    `sys_org_type` varchar(32)  NULL DEFAULT NULL COMMENT '组织类型',
    `enabled`      tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `sort_no`      int          NOT NULL DEFAULT 0 COMMENT '排序号',
    `remark`       varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`    varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`    varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`      bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`      tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_org_id`) USING BTREE,
    UNIQUE KEY `uk_sys_org_code_deleted` (`sys_org_code`, `deleted`) USING BTREE,
    KEY `idx_sys_org_parent_sort_deleted` (`parent_id`, `sort_no`, `deleted`) USING BTREE,
    KEY `idx_sys_org_deleted_create_time` (`deleted`, `create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统组织';

CREATE TABLE IF NOT EXISTS `sys_role`
(
    `sys_role_id`   varchar(32)  NOT NULL COMMENT '角色主键',
    `parent_id`     varchar(32)  NULL DEFAULT NULL COMMENT '上级角色主键',
    `sys_role_code` varchar(64)  NOT NULL COMMENT '角色编码',
    `sys_role_name` varchar(128) NOT NULL COMMENT '角色名称',
    `enabled`       tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `sort_no`       int          NOT NULL DEFAULT 0 COMMENT '排序号',
    `remark`        varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`     varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`     varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`       tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_role_id`) USING BTREE,
    UNIQUE KEY `uk_sys_role_code_deleted` (`sys_role_code`, `deleted`) USING BTREE,
    KEY `idx_sys_role_parent_sort_deleted` (`parent_id`, `sort_no`, `deleted`) USING BTREE,
    KEY `idx_sys_role_deleted_create_time` (`deleted`, `create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统角色';

CREATE TABLE IF NOT EXISTS `sys_res`
(
    `sys_res_id`     varchar(32)  NOT NULL COMMENT '资源主键',
    `parent_id`      varchar(32)  NULL DEFAULT NULL COMMENT '上级资源主键',
    `sys_res_code`   varchar(64)  NOT NULL COMMENT '资源编码',
    `sys_res_name`   varchar(128) NOT NULL COMMENT '资源名称',
    `sys_res_type`   varchar(32)  NOT NULL COMMENT '资源类型：menu、button、api',
    `permission_code` varchar(128) NULL DEFAULT NULL COMMENT '权限编码',
    `route_path`     varchar(255) NULL DEFAULT NULL COMMENT '前端路由路径',
    `component_path` varchar(255) NULL DEFAULT NULL COMMENT '前端组件路径',
    `icon`           varchar(128) NULL DEFAULT NULL COMMENT '图标',
    `visibled`       tinyint      NOT NULL DEFAULT 1 COMMENT '是否可见：1可见，0隐藏',
    `enabled`        tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `sort_no`        int          NOT NULL DEFAULT 0 COMMENT '排序号',
    `sys_data`       json         NULL COMMENT '资源扩展数据',
    `remark`         varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`      varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`      varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`        bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`        tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_res_id`) USING BTREE,
    UNIQUE KEY `uk_sys_res_code_deleted` (`sys_res_code`, `deleted`) USING BTREE,
    KEY `idx_sys_res_parent_sort_deleted` (`parent_id`, `sort_no`, `deleted`) USING BTREE,
    KEY `idx_sys_res_type_enabled_deleted` (`sys_res_type`, `enabled`, `deleted`) USING BTREE,
    KEY `idx_sys_res_permission_deleted` (`permission_code`, `deleted`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统资源';

CREATE TABLE IF NOT EXISTS `sys_role_auth`
(
    `sys_role_auth_id` varchar(32) NOT NULL COMMENT '角色授权主键',
    `sys_role_id`      varchar(32) NOT NULL COMMENT '角色主键',
    `sys_res_id`       varchar(32) NOT NULL COMMENT '资源主键',
    `create_id`        varchar(32) NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`      datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`        varchar(32) NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`      datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint      NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`          tinyint     NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_role_auth_id`) USING BTREE,
    UNIQUE KEY `uk_sys_role_auth_role_res_deleted` (`sys_role_id`, `sys_res_id`, `deleted`) USING BTREE,
    KEY `idx_sys_role_auth_res_deleted` (`sys_res_id`, `deleted`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色资源授权';

CREATE TABLE IF NOT EXISTS `sys_rights`
(
    `sys_rights_id` varchar(32)  NOT NULL COMMENT '权限身份主键',
    `sys_user_id`   varchar(32)  NOT NULL COMMENT '用户主键',
    `sys_org_id`    varchar(32)  NOT NULL COMMENT '组织主键',
    `sys_role_id`   varchar(32)  NOT NULL COMMENT '角色主键',
    `defaulted`     tinyint      NOT NULL DEFAULT 0 COMMENT '是否默认身份：1默认，0普通',
    `enabled`       tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `data_scope`    varchar(32)  NOT NULL DEFAULT 'self' COMMENT '数据范围：self、sys-org、sys-org-and-children、all、custom',
    `remark`        varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`     varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`     varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`       tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_rights_id`) USING BTREE,
    UNIQUE KEY `uk_sys_rights_user_org_role_deleted` (`sys_user_id`, `sys_org_id`, `sys_role_id`, `deleted`) USING BTREE,
    KEY `idx_sys_rights_user_enabled_deleted` (`sys_user_id`, `enabled`, `deleted`) USING BTREE,
    KEY `idx_sys_rights_user_defaulted` (`sys_user_id`, `defaulted`, `enabled`, `deleted`) USING BTREE,
    KEY `idx_sys_rights_org_enabled_deleted` (`sys_org_id`, `enabled`, `deleted`) USING BTREE,
    KEY `idx_sys_rights_role_enabled_deleted` (`sys_role_id`, `enabled`, `deleted`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户组织角色权限身份';

CREATE TABLE IF NOT EXISTS `sys_rights_extra`
(
    `sys_rights_extra_id` varchar(32) NOT NULL COMMENT '权限额外组织主键',
    `sys_rights_id`       varchar(32) NOT NULL COMMENT '权限身份主键',
    `sys_org_id`          varchar(32) NOT NULL COMMENT '组织主键',
    `create_id`           varchar(32) NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`           varchar(32) NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`             bigint      NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`             tinyint     NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_rights_extra_id`) USING BTREE,
    UNIQUE KEY `uk_sys_rights_extra_rights_org_deleted` (`sys_rights_id`, `sys_org_id`, `deleted`) USING BTREE,
    KEY `idx_sys_rights_extra_org_deleted` (`sys_org_id`, `deleted`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='权限额外组织范围';

-- ---------------------------------------------------------------------------
-- basedata module
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `sys_dict`
(
    `sys_dict_id`   varchar(32)  NOT NULL COMMENT '字典主键',
    `sys_dict_code` varchar(64)  NOT NULL COMMENT '字典编码',
    `sys_dict_name` varchar(128) NOT NULL COMMENT '字典名称',
    `enabled`       tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `remark`        varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`     varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`     varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`       tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_dict_id`) USING BTREE,
    UNIQUE KEY `uk_sys_dict_code_deleted` (`sys_dict_code`, `deleted`) USING BTREE,
    KEY `idx_sys_dict_deleted_create_time` (`deleted`, `create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统字典';

CREATE TABLE IF NOT EXISTS `sys_dict_item`
(
    `sys_dict_item_id` varchar(32)  NOT NULL COMMENT '字典项主键',
    `sys_dict_id`      varchar(32)  NOT NULL COMMENT '字典主键',
    `dict_item_value`  varchar(128) NOT NULL COMMENT '字典项值',
    `dict_item_label`  varchar(128) NOT NULL COMMENT '字典项标签',
    `enabled`          tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `sort_no`          int          NOT NULL DEFAULT 0 COMMENT '排序号',
    `remark`           varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`        varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`        varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`          tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_dict_item_id`) USING BTREE,
    UNIQUE KEY `uk_sys_dict_item_dict_value_deleted` (`sys_dict_id`, `dict_item_value`, `deleted`) USING BTREE,
    KEY `idx_sys_dict_item_dict_sort_deleted` (`sys_dict_id`, `sort_no`, `deleted`) USING BTREE,
    KEY `idx_sys_dict_item_value_deleted` (`dict_item_value`, `deleted`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统字典项';

CREATE TABLE IF NOT EXISTS `sys_config`
(
    `sys_config_id`    varchar(32)  NOT NULL COMMENT '配置主键',
    `sys_config_code`  varchar(64)  NOT NULL COMMENT '配置编码',
    `sys_config_name`  varchar(128) NOT NULL COMMENT '配置名称',
    `sys_config_value` text         NULL COMMENT '配置值',
    `value_type`       varchar(32)  NOT NULL DEFAULT 'string' COMMENT '配置值类型',
    `editable`         tinyint      NOT NULL DEFAULT 1 COMMENT '是否允许编辑：1允许，0禁止',
    `enabled`          tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `remark`           varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`        varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`        varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`          bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`          tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_config_id`) USING BTREE,
    UNIQUE KEY `uk_sys_config_code_deleted` (`sys_config_code`, `deleted`) USING BTREE,
    KEY `idx_sys_config_deleted_create_time` (`deleted`, `create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统配置';

CREATE TABLE IF NOT EXISTS `sys_serial`
(
    `sys_serial_id` varchar(32)  NOT NULL COMMENT '流水号主键',
    `serial_code`   varchar(64)  NOT NULL COMMENT '流水号编码',
    `serial_name`   varchar(128) NOT NULL COMMENT '流水号名称',
    `serial_prefix` varchar(64)  NULL DEFAULT NULL COMMENT '流水号前缀',
    `date_pattern`  varchar(32)  NULL DEFAULT NULL COMMENT '日期格式',
    `serial_length` int          NOT NULL DEFAULT 6 COMMENT '流水号长度',
    `current_value` bigint       NOT NULL DEFAULT 0 COMMENT '当前值',
    `step_value`    int          NOT NULL DEFAULT 1 COMMENT '步长',
    `cycle_type`    varchar(32)  NOT NULL DEFAULT 'none' COMMENT '循环类型：none、day、month、year',
    `last_cycle`    varchar(32)  NULL DEFAULT NULL COMMENT '最后循环标识',
    `enabled`       tinyint      NOT NULL DEFAULT 1 COMMENT '启用状态：1启用，0禁用',
    `remark`        varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `create_id`     varchar(32)  NULL DEFAULT NULL COMMENT '创建人主键',
    `create_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_id`     varchar(32)  NULL DEFAULT NULL COMMENT '更新人主键',
    `update_time`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`       bigint       NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `deleted`       tinyint      NOT NULL DEFAULT 0 COMMENT '删除标记：1删除，0未删除',
    PRIMARY KEY (`sys_serial_id`) USING BTREE,
    UNIQUE KEY `uk_sys_serial_code_deleted` (`serial_code`, `deleted`) USING BTREE,
    KEY `idx_sys_serial_deleted_create_time` (`deleted`, `create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='系统流水号';
