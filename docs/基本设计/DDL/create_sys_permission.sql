-- ============================================================
-- 权限模块 DDL: 用户-菜单权限 + 操作权限
-- ============================================================

-- 用户-菜单权限（用户能看到哪些菜单）
CREATE TABLE sys_user_menu (
    user_id     VARCHAR2(36) NOT NULL,
    menu_id     VARCHAR2(36) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, menu_id)
);

COMMENT ON TABLE sys_user_menu IS '用户菜单权限';
COMMENT ON COLUMN sys_user_menu.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_menu.menu_id IS '菜单ID';

-- 操作定义（每个菜单下有哪些可执行操作）
CREATE TABLE sys_action (
    id          VARCHAR2(36) PRIMARY KEY,
    menu_id     VARCHAR2(36) NOT NULL,
    action_code VARCHAR2(50) NOT NULL,
    action_name VARCHAR2(100) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE sys_action IS '操作定义';
COMMENT ON COLUMN sys_action.id IS '操作ID';
COMMENT ON COLUMN sys_action.menu_id IS '所属菜单ID';
COMMENT ON COLUMN sys_action.action_code IS '操作代码(view/create/edit/delete/toggle_status)';
COMMENT ON COLUMN sys_action.action_name IS '操作名称(查看/新建/编辑/删除/启用停用)';

CREATE INDEX idx_sys_action_menu ON sys_action(menu_id);

-- 用户-操作权限（用户能执行哪些操作）
CREATE TABLE sys_user_action (
    user_id     VARCHAR2(36) NOT NULL,
    action_id   VARCHAR2(36) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, action_id)
);

COMMENT ON TABLE sys_user_action IS '用户操作权限';
COMMENT ON COLUMN sys_user_action.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_action.action_id IS '操作ID';
