delete from sys_user;

insert into sys_user (
    sys_user_id,
    sys_user_code,
    sys_user_pwd,
    sys_user_name,
    sys_user_mobile,
    sys_user_email,
    avatar_file_id,
    sys_user_status,
    sys_data,
    create_id,
    create_time,
    update_id,
    update_time,
    version,
    deleted
) values (
    '00000000000000000000000000000001',
    'admin',
    '{bcrypt}$2a$10$EaSXc4R7fELnb/VfacH4o.OnWQD7ZhYrrJjtDsSxU39GpL//hYcBa',
    '管理员',
    '13800000000',
    'admin@example.com',
    null,
    1,
    null,
    'system',
    current_timestamp,
    'system',
    current_timestamp,
    0,
    0
);

insert into sys_user (
    sys_user_id,
    sys_user_code,
    sys_user_pwd,
    sys_user_name,
    sys_user_mobile,
    sys_user_email,
    avatar_file_id,
    sys_user_status,
    sys_data,
    create_id,
    create_time,
    update_id,
    update_time,
    version,
    deleted
) values (
    '00000000000000000000000000000002',
    'disabled',
    '{bcrypt}$2a$10$EaSXc4R7fELnb/VfacH4o.OnWQD7ZhYrrJjtDsSxU39GpL//hYcBa',
    '禁用用户',
    '13900000000',
    'disabled@example.com',
    null,
    0,
    null,
    'system',
    current_timestamp,
    'system',
    current_timestamp,
    0,
    0
);
