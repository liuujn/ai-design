-- ============================================================
-- 权限模块种子数据
-- ============================================================

-- 1. 操作定义：为非分隔线菜单定义可用操作（使用英文避免 JA16SJIS 编码问题）
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m001_view', 'm001', 'view', 'Home');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m003_view', 'm003', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m003_create', 'm003', 'create', 'Create');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m003_edit', 'm003', 'edit', 'Edit');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m003_delete', 'm003', 'delete', 'Delete');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m003_toggle_status', 'm003', 'toggle_status', 'Enable/Disable');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m004_view', 'm004', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m004_create', 'm004', 'create', 'Create');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m004_edit', 'm004', 'edit', 'Edit');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m004_delete', 'm004', 'delete', 'Delete');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m004_set_default', 'm004', 'set_default', 'Set Default');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m005_view', 'm005', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m005_create', 'm005', 'create', 'Create');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m005_edit', 'm005', 'edit', 'Edit');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m005_delete', 'm005', 'delete', 'Delete');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m006_view', 'm006', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m006_create', 'm006', 'create', 'Create');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m006_edit', 'm006', 'edit', 'Edit');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m006_delete', 'm006', 'delete', 'Delete');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m006_toggle_status', 'm006', 'toggle_status', 'Enable/Disable');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m007_view', 'm007', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m007_edit', 'm007', 'edit', 'Edit');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m008_view', 'm008', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m008_checkout', 'm008', 'checkout', 'Checkout');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m010_view', 'm010', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m010_create', 'm010', 'create', 'Create');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m010_edit', 'm010', 'edit', 'Edit');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m010_delete', 'm010', 'delete', 'Delete');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m010_toggle_status', 'm010', 'toggle_status', 'Enable/Disable');

INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m011_view', 'm011', 'view', 'View');
INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES ('act_m011_edit', 'm011', 'edit', 'Edit');

-- 2. 为 admin 用户(u001)赋予所有菜单和操作权限
INSERT INTO sys_user_menu (user_id, menu_id)
SELECT 'u001', id FROM sys_menus WHERE is_deleted = 0;

INSERT INTO sys_user_action (user_id, action_id)
SELECT 'u001', id FROM sys_action;

COMMIT;
