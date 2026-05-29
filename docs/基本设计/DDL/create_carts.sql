CREATE TABLE carts (
    id VARCHAR2(36) PRIMARY KEY,
    user_id VARCHAR2(36) NOT NULL,
    address_id VARCHAR2(36),
    status VARCHAR2(20) DEFAULT 'active',
    total_amount NUMBER(12,2) DEFAULT 0,
    remark VARCHAR2(500),
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    created_by VARCHAR2(36),
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_by VARCHAR2(36),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE INDEX idx_carts_user ON carts(user_id);
CREATE INDEX idx_carts_status ON carts(status);

CREATE TABLE cart_items (
    id VARCHAR2(36) PRIMARY KEY,
    cart_id VARCHAR2(36) NOT NULL,
    product_id VARCHAR2(36) NOT NULL,
    product_name VARCHAR2(200) NOT NULL,
    quantity NUMBER(10) DEFAULT 1,
    unit_price NUMBER(12,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    created_by VARCHAR2(36),
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_by VARCHAR2(36),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product ON cart_items(product_id);
