create table if not exists sys_user
(
    sys_user_id     varchar(32)  not null comment '用户ID',
    sys_user_code   varchar(64)  not null comment '用户账号',
    sys_user_pwd    varchar(128) not null comment '用户密码',
    sys_user_name   varchar(64)  not null comment '用户名称',
    sys_user_mobile varchar(32)  null comment '手机号',
    sys_user_email  varchar(128) null comment '邮箱',
    avatar_file_id  varchar(64)  null comment '头像文件ID',
    sys_user_status tinyint      not null default 1 comment '用户状态：1启用，0禁用',
    sys_data        json         null comment '扩展数据',
    create_id       varchar(32)  null comment '创建人',
    create_time     datetime     null comment '创建时间',
    update_id       varchar(32)  null comment '更新人',
    update_time     datetime     null comment '更新时间',
    version         int          not null default 0 comment '乐观锁版本',
    deleted         tinyint      not null default 0 comment '逻辑删除：0未删除，1已删除',
    primary key (sys_user_id),
    unique key uk_sys_user_code (sys_user_code, deleted)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci
  comment = '系统用户';
