create table if not exists sys_user
(
    sys_user_id     varchar(32)  not null primary key,
    sys_user_code   varchar(64)  not null,
    sys_user_pwd    varchar(128) not null,
    sys_user_name   varchar(64)  not null,
    sys_user_mobile varchar(32),
    sys_user_email  varchar(128),
    avatar_file_id  varchar(64),
    sys_user_status int          not null default 1,
    sys_data        clob,
    create_id       varchar(32),
    create_time     timestamp,
    update_id       varchar(32),
    update_time     timestamp,
    version         int          not null default 0,
    deleted         int          not null default 0
);

create unique index if not exists uk_sys_user_code on sys_user (sys_user_code, deleted);
