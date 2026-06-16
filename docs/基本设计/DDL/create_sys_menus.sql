CREATE TABLE sys_menus (
    id VARCHAR2(36) PRIMARY KEY,
    parent_id VARCHAR2(36),
    label VARCHAR2(100) NOT NULL,
    icon VARCHAR2(2000),
    page_title VARCHAR2(100),
    page_src VARCHAR2(500),
    sort_order NUMBER(5) DEFAULT 0,
    is_divider NUMBER(1) DEFAULT 0,
    status VARCHAR2(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    created_by VARCHAR2(36),
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_by VARCHAR2(36),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE INDEX idx_sys_menus_parent ON sys_menus(parent_id);
CREATE INDEX idx_sys_menus_status ON sys_menus(status);
CREATE INDEX idx_sys_menus_sort ON sys_menus(sort_order);

COMMENT ON TABLE sys_menus IS 'menu master';
COMMENT ON COLUMN sys_menus.id IS 'PK';
COMMENT ON COLUMN sys_menus.parent_id IS 'parent menu id';
COMMENT ON COLUMN sys_menus.label IS 'display label';
COMMENT ON COLUMN sys_menus.icon IS 'SVG icon';
COMMENT ON COLUMN sys_menus.page_title IS 'header title';
COMMENT ON COLUMN sys_menus.page_src IS 'page html path';
COMMENT ON COLUMN sys_menus.sort_order IS 'sort order';
COMMENT ON COLUMN sys_menus.is_divider IS '1=divider 0=menu';
COMMENT ON COLUMN sys_menus.status IS 'active/inactive';
