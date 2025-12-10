TRUNCATE TABLE
    sys_role_permission,
    sys_user_role,
    sys_permission,
    sys_role,
    sys_user
    RESTART IDENTITY;


INSERT INTO sys_permission (id, code, name, description, resource, action, created_at, updated_at)
VALUES (gen_random_uuid(), 'user:read', '用户-查看', '查看用户列表/详情', 'user', 'read', now(), now()),
       (gen_random_uuid(), 'user:write', '用户-编辑', '新增/编辑用户', 'user', 'write', now(), now()),
       (gen_random_uuid(), 'role:read', '角色-查看', '查看角色', 'role', 'read', now(), now()),
       (gen_random_uuid(), 'role:write', '角色-编辑', '新增/编辑角色', 'role', 'write', now(), now()),
       (gen_random_uuid(), 'order:read', '订单-查看', '查看订单', 'order', 'read', now(), now()),
       (gen_random_uuid(), 'order:pay', '订单-支付', '支付订单', 'order', 'pay', now(), now()),
       (gen_random_uuid(), 'student:find', '学生-查询', '查询学生信息', 'student', 'find', now(), now()),
       (gen_random_uuid(), 'student:del', '学生-删除', '删除学生信息', 'student', 'del', now(), now());
INSERT INTO sys_role (id, code, name, description, created_at, updated_at)
VALUES (gen_random_uuid(), 'ADMIN', '管理员', '系统管理员，拥有全部权限', now(), now()),
       (gen_random_uuid(), 'NORMAL', '普通用户', '普通用户，拥有基础权限', now(), now());
-- ADMIN -> ALL
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM sys_role r
         CROSS JOIN sys_permission p
WHERE r.code = 'ADMIN';

-- NORMAL -> 部分权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM sys_role r
         JOIN sys_permission p ON p.code IN ('user:read', 'role:read', 'order:read', 'student:find')
WHERE r.code = 'NORMAL';

-- 密码都是 123456
-- BCrypt 哈希值: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH (这只是示例，请使用下面的真实哈希)
INSERT INTO sys_user (id, username, password, email, phone,
                      status, enabled, account_non_expired, account_non_locked, credentials_non_expired,
                      failed_login_attempts, lock_until, last_login_at,
                      created_at, updated_at)
VALUES
    -- admin / 123456
    (gen_random_uuid(), 'admin', '$2a$10$YourActualBcryptHashHere1234567890123456789012', 'admin@test.com',
     '13800000001',
     'ACTIVE', true, true, true, true, 0, NULL, now(), now(), now()),

    -- jack / 123456
    (gen_random_uuid(), 'jack', '$2a$10$YourActualBcryptHashHere1234567890123456789012', 'jack@test.com', '13800000002',
     'ACTIVE', true, true, true, true, 0, NULL, now(), now(), now()),

    -- locked / 123456 (账户已锁定)
    (gen_random_uuid(), 'locked', '$2a$10$YourActualBcryptHashHere1234567890123456789012', 'locked@test.com',
     '13800000003',
     'LOCKED', true, true, false, true, 5, now() + interval '30 minutes', NULL, now(), now()),

    -- disabled / 123456 (账户已禁用)
    (gen_random_uuid(), 'disabled', '$2a$10$YourActualBcryptHashHere1234567890123456789012', 'disabled@test.com',
     '13800000004',
     'DISABLED', false, true, true, true, 0, NULL, NULL, now(), now());
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id
FROM sys_user u
         JOIN sys_role r ON r.code = 'ADMIN'
WHERE u.username = 'admin';

INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id
FROM sys_user u
         JOIN sys_role r ON r.code = 'NORMAL'
WHERE u.username IN ('jack', 'locked', 'disabled');
