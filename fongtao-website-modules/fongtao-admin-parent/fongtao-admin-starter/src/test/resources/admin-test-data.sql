delete from sys_rights_extra;
delete from sys_role_auth;
delete from sys_rights;
delete from sys_dict_item;
delete from sys_serial;
delete from sys_config;
delete from sys_dict;
delete from sys_res;
delete from sys_role;
delete from sys_org;
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
    sys_user_id, sys_user_code, sys_user_pwd, sys_user_name, sys_user_status,
    create_id, create_time, update_id, update_time, version, deleted
) values (
    '00000000000000000000000000000003', 'operator',
    '{bcrypt}$2a$10$EaSXc4R7fELnb/VfacH4o.OnWQD7ZhYrrJjtDsSxU39GpL//hYcBa', '普通操作员', 1,
    'system', current_timestamp, 'system', current_timestamp, 0, 0
);

insert into sys_org (
    sys_org_id, parent_id, sys_org_code, sys_org_name, enabled, sort_no,
    create_id, create_time, update_id, update_time, version, deleted
) values (
    '10000000000000000000000000000001', null, 'root', '默认组织', 1, 0,
    'system', current_timestamp, 'system', current_timestamp, 0, 0
);

insert into sys_role (
    sys_role_id, parent_id, sys_role_code, sys_role_name, enabled, sort_no,
    create_id, create_time, update_id, update_time, version, deleted
) values
    ('20000000000000000000000000000001', null, 'administrator', '管理员', 1, 0,
     'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('20000000000000000000000000000002', null, 'operator', '普通操作员', 1, 1,
     'system', current_timestamp, 'system', current_timestamp, 0, 0);

insert into sys_res (
    sys_res_id, parent_id, sys_res_code, sys_res_name, sys_res_type, permission_code,
    visibled, enabled, sort_no, create_id, create_time, update_id, update_time, version, deleted
) values
    ('30000000000000000000000000000001', null, 'admin', '后台管理', 'menu', null, 1, 1, 0, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000002', '30000000000000000000000000000001', 'admin-sys-user-page', '查询用户分页', 'api', 'admin:sys-user:page', 0, 1, 1, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000003', '30000000000000000000000000000001', 'admin-sys-user-get-by-id', '查询用户详情', 'api', 'admin:sys-user:get-by-id', 0, 1, 2, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000004', '30000000000000000000000000000001', 'admin-sys-user-create', '新增用户', 'api', 'admin:sys-user:create', 0, 1, 3, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000005', '30000000000000000000000000000001', 'admin-sys-user-update-by-id', '更新用户', 'api', 'admin:sys-user:update-by-id', 0, 1, 4, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000006', '30000000000000000000000000000001', 'admin-sys-user-delete-by-id', '删除用户', 'api', 'admin:sys-user:delete-by-id', 0, 1, 5, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000007', '30000000000000000000000000000001', 'admin-sys-user-update-status', '更新用户状态', 'api', 'admin:sys-user:update-status', 0, 1, 6, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000008', '30000000000000000000000000000001', 'admin-sys-user-update-password', '更新用户密码', 'api', 'admin:sys-user:update-password', 0, 1, 7, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000009', '30000000000000000000000000000001', 'admin-sys-org-page', '查询组织分页', 'api', 'admin:sys-org:page', 0, 1, 8, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000010', '30000000000000000000000000000001', 'admin-sys-org-tree', '查询组织树', 'api', 'admin:sys-org:tree', 0, 1, 9, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000011', '30000000000000000000000000000001', 'admin-sys-org-get-by-id', '查询组织详情', 'api', 'admin:sys-org:get-by-id', 0, 1, 10, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000012', '30000000000000000000000000000001', 'admin-sys-org-create', '新增组织', 'api', 'admin:sys-org:create', 0, 1, 11, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000013', '30000000000000000000000000000001', 'admin-sys-org-update-by-id', '更新组织', 'api', 'admin:sys-org:update-by-id', 0, 1, 12, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000014', '30000000000000000000000000000001', 'admin-sys-org-delete-by-id', '删除组织', 'api', 'admin:sys-org:delete-by-id', 0, 1, 13, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000015', '30000000000000000000000000000001', 'admin-sys-role-page', '查询角色分页', 'api', 'admin:sys-role:page', 0, 1, 14, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000016', '30000000000000000000000000000001', 'admin-sys-role-tree', '查询角色树', 'api', 'admin:sys-role:tree', 0, 1, 15, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000017', '30000000000000000000000000000001', 'admin-sys-role-get-by-id', '查询角色详情', 'api', 'admin:sys-role:get-by-id', 0, 1, 16, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000018', '30000000000000000000000000000001', 'admin-sys-role-create', '新增角色', 'api', 'admin:sys-role:create', 0, 1, 17, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000019', '30000000000000000000000000000001', 'admin-sys-role-update-by-id', '更新角色', 'api', 'admin:sys-role:update-by-id', 0, 1, 18, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000020', '30000000000000000000000000000001', 'admin-sys-role-delete-by-id', '删除角色', 'api', 'admin:sys-role:delete-by-id', 0, 1, 19, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000021', '30000000000000000000000000000001', 'admin-sys-res-page', '查询资源分页', 'api', 'admin:sys-res:page', 0, 1, 20, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000022', '30000000000000000000000000000001', 'admin-sys-res-tree', '查询资源树', 'api', 'admin:sys-res:tree', 0, 1, 21, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000023', '30000000000000000000000000000001', 'admin-sys-res-get-by-id', '查询资源详情', 'api', 'admin:sys-res:get-by-id', 0, 1, 22, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000024', '30000000000000000000000000000001', 'admin-sys-res-create', '新增资源', 'api', 'admin:sys-res:create', 0, 1, 23, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000025', '30000000000000000000000000000001', 'admin-sys-res-update-by-id', '更新资源', 'api', 'admin:sys-res:update-by-id', 0, 1, 24, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000026', '30000000000000000000000000000001', 'admin-sys-res-delete-by-id', '删除资源', 'api', 'admin:sys-res:delete-by-id', 0, 1, 25, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000027', '30000000000000000000000000000001', 'admin-sys-role-auth-list-auth-res', '查询角色授权资源', 'api', 'admin:sys-role-auth:list-auth-res', 0, 1, 26, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000028', '30000000000000000000000000000001', 'admin-sys-role-auth-auth-res', '授权角色资源', 'api', 'admin:sys-role-auth:auth-res', 0, 1, 27, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000029', '30000000000000000000000000000001', 'admin-sys-rights-page', '查询用户组织角色身份分页', 'api', 'admin:sys-rights:page', 0, 1, 28, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000030', '30000000000000000000000000000001', 'admin-sys-rights-get-by-id', '查询用户组织角色身份详情', 'api', 'admin:sys-rights:get-by-id', 0, 1, 29, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000031', '30000000000000000000000000000001', 'admin-sys-rights-create', '新增用户组织角色身份', 'api', 'admin:sys-rights:create', 0, 1, 30, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000032', '30000000000000000000000000000001', 'admin-sys-rights-update-by-id', '更新用户组织角色身份', 'api', 'admin:sys-rights:update-by-id', 0, 1, 31, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000033', '30000000000000000000000000000001', 'admin-sys-rights-update-enabled', '更新身份状态', 'api', 'admin:sys-rights:update-enabled', 0, 1, 32, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000034', '30000000000000000000000000000001', 'admin-sys-rights-delete-by-id', '删除用户组织角色身份', 'api', 'admin:sys-rights:delete-by-id', 0, 1, 33, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000035', '30000000000000000000000000000001', 'admin-sys-dict-page', '查询字典分页', 'api', 'admin:sys-dict:page', 0, 1, 34, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000036', '30000000000000000000000000000001', 'admin-sys-dict-get-by-id', '查询字典详情', 'api', 'admin:sys-dict:get-by-id', 0, 1, 35, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000037', '30000000000000000000000000000001', 'admin-sys-dict-create', '新增字典', 'api', 'admin:sys-dict:create', 0, 1, 36, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000038', '30000000000000000000000000000001', 'admin-sys-dict-update-by-id', '更新字典', 'api', 'admin:sys-dict:update-by-id', 0, 1, 37, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000039', '30000000000000000000000000000001', 'admin-sys-dict-delete-by-id', '删除字典', 'api', 'admin:sys-dict:delete-by-id', 0, 1, 38, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000040', '30000000000000000000000000000001', 'admin-sys-dict-options', '查询字典选项', 'api', 'admin:sys-dict:options', 0, 1, 39, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000041', '30000000000000000000000000000001', 'admin-sys-dict-item-page', '查询字典项分页', 'api', 'admin:sys-dict-item:page', 0, 1, 40, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000042', '30000000000000000000000000000001', 'admin-sys-dict-item-get-by-id', '查询字典项详情', 'api', 'admin:sys-dict-item:get-by-id', 0, 1, 41, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000043', '30000000000000000000000000000001', 'admin-sys-dict-item-create', '新增字典项', 'api', 'admin:sys-dict-item:create', 0, 1, 42, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000044', '30000000000000000000000000000001', 'admin-sys-dict-item-update-by-id', '更新字典项', 'api', 'admin:sys-dict-item:update-by-id', 0, 1, 43, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000045', '30000000000000000000000000000001', 'admin-sys-dict-item-delete-by-id', '删除字典项', 'api', 'admin:sys-dict-item:delete-by-id', 0, 1, 44, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000046', '30000000000000000000000000000001', 'admin-sys-config-page', '查询配置分页', 'api', 'admin:sys-config:page', 0, 1, 45, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000047', '30000000000000000000000000000001', 'admin-sys-config-get-by-id', '查询配置详情', 'api', 'admin:sys-config:get-by-id', 0, 1, 46, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000048', '30000000000000000000000000000001', 'admin-sys-config-create', '新增配置', 'api', 'admin:sys-config:create', 0, 1, 47, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000049', '30000000000000000000000000000001', 'admin-sys-config-update-by-id', '更新配置', 'api', 'admin:sys-config:update-by-id', 0, 1, 48, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000050', '30000000000000000000000000000001', 'admin-sys-config-delete-by-id', '删除配置', 'api', 'admin:sys-config:delete-by-id', 0, 1, 49, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000051', '30000000000000000000000000000001', 'admin-sys-serial-page', '查询流水号分页', 'api', 'admin:sys-serial:page', 0, 1, 50, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000052', '30000000000000000000000000000001', 'admin-sys-serial-get-by-id', '查询流水号详情', 'api', 'admin:sys-serial:get-by-id', 0, 1, 51, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000053', '30000000000000000000000000000001', 'admin-sys-serial-create', '新增流水号', 'api', 'admin:sys-serial:create', 0, 1, 52, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000054', '30000000000000000000000000000001', 'admin-sys-serial-update-by-id', '更新流水号', 'api', 'admin:sys-serial:update-by-id', 0, 1, 53, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000055', '30000000000000000000000000000001', 'admin-sys-serial-delete-by-id', '删除流水号', 'api', 'admin:sys-serial:delete-by-id', 0, 1, 54, 'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('30000000000000000000000000000056', '30000000000000000000000000000001', 'admin-sys-serial-next', '生成流水号', 'api', 'admin:sys-serial:next', 0, 1, 55, 'system', current_timestamp, 'system', current_timestamp, 0, 0);

insert into sys_role_auth (
    sys_role_auth_id, sys_role_id, sys_res_id, create_id, create_time, update_id, update_time, version, deleted
) select
    concat('400000000000000000000000000000', right(concat('00', row_number() over (order by sys_res_id)), 2)),
    '20000000000000000000000000000001', sys_res_id,
    'system', current_timestamp, 'system', current_timestamp, 0, 0
from sys_res;

insert into sys_rights (
    sys_rights_id, sys_user_id, sys_org_id, sys_role_id, defaulted, enabled, data_scope,
    create_id, create_time, update_id, update_time, version, deleted
) values
    ('50000000000000000000000000000001', '00000000000000000000000000000001',
     '10000000000000000000000000000001', '20000000000000000000000000000001', 1, 1, 'all',
     'system', current_timestamp, 'system', current_timestamp, 0, 0),
    ('50000000000000000000000000000002', '00000000000000000000000000000003',
     '10000000000000000000000000000001', '20000000000000000000000000000002', 1, 1, 'self',
     'system', current_timestamp, 'system', current_timestamp, 0, 0);

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
