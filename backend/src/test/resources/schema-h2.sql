CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(64) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    display_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    password_hash VARCHAR(255),
    status VARCHAR(20) DEFAULT 'active',
    mfa_enabled BOOLEAN DEFAULT FALSE,
    mfa_secret VARCHAR(100),
    created_at TIMESTAMP,
    created_by VARCHAR(64),
    updated_at TIMESTAMP,
    updated_by VARCHAR(64),
    is_deleted INT DEFAULT 0
);
