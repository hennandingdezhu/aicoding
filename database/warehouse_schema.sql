CREATE DATABASE IF NOT EXISTS warehouse_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE warehouse_system;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    real_name VARCHAR(64) NOT NULL,
    phone VARCHAR(32),
    email VARCHAR(128),
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1 enabled, 0 disabled',
    last_login_time DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(64) NOT NULL UNIQUE,
    role_name VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色';

CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT,
    permission_code VARCHAR(128) NOT NULL UNIQUE,
    permission_name VARCHAR(128) NOT NULL,
    permission_type VARCHAR(32) NOT NULL COMMENT 'MENU/BUTTON/API',
    path VARCHAR(255),
    component VARCHAR(255),
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限';

CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系';

CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    KEY idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关系';

CREATE TABLE warehouse (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_code VARCHAR(64) NOT NULL UNIQUE,
    warehouse_name VARCHAR(128) NOT NULL,
    address VARCHAR(255),
    manager_name VARCHAR(64),
    manager_phone VARCHAR(32),
    status TINYINT NOT NULL DEFAULT 1,
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库';

CREATE TABLE sys_user_warehouse (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_warehouse (user_id, warehouse_id),
    KEY idx_warehouse_id (warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户仓库数据权限';

CREATE TABLE warehouse_area (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    area_code VARCHAR(64) NOT NULL,
    area_name VARCHAR(128) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_warehouse_area_code (warehouse_id, area_code),
    KEY idx_warehouse_id (warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库区';

CREATE TABLE warehouse_location (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    location_code VARCHAR(64) NOT NULL UNIQUE,
    location_name VARCHAR(128) NOT NULL,
    capacity DECIMAL(18,4),
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1 enabled, 0 disabled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_warehouse_area (warehouse_id, area_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位';

CREATE TABLE product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT,
    category_code VARCHAR(64) NOT NULL UNIQUE,
    category_name VARCHAR(128) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类';

CREATE TABLE file_resource (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(64),
    file_size BIGINT,
    oss_key VARCHAR(512) NOT NULL,
    bucket_name VARCHAR(128),
    business_type VARCHAR(64),
    business_id BIGINT,
    uploaded_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_business (business_type, business_id),
    KEY idx_oss_key (oss_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件资源';

CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(64) NOT NULL UNIQUE,
    product_name VARCHAR(128) NOT NULL,
    category_id BIGINT NOT NULL,
    specification VARCHAR(255),
    unit VARCHAR(32) NOT NULL,
    brand VARCHAR(64),
    cost_price DECIMAL(18,4),
    sale_price DECIMAL(18,4),
    image_file_id BIGINT,
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(500),
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_category_id (category_id),
    KEY idx_product_name (product_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品';

CREATE TABLE supplier (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_code VARCHAR(64) NOT NULL UNIQUE,
    supplier_name VARCHAR(128) NOT NULL,
    contact_name VARCHAR(64),
    contact_phone VARCHAR(32),
    address VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商';

CREATE TABLE customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_code VARCHAR(64) NOT NULL UNIQUE,
    customer_name VARCHAR(128) NOT NULL,
    contact_name VARCHAR(64),
    contact_phone VARCHAR(32),
    address VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户';

CREATE TABLE stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    total_quantity DECIMAL(18,4) NOT NULL DEFAULT 0,
    available_quantity DECIMAL(18,4) NOT NULL DEFAULT 0,
    locked_quantity DECIMAL(18,4) NOT NULL DEFAULT 0,
    warning_min DECIMAL(18,4),
    warning_max DECIMAL(18,4),
    version INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_stock_location_product (product_id, warehouse_id, area_id, location_id),
    KEY idx_warehouse_product (warehouse_id, product_id),
    KEY idx_location_id (location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存';

CREATE TABLE inbound_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    inbound_type VARCHAR(32) NOT NULL COMMENT 'PURCHASE/RETURN/TRANSFER/OTHER',
    warehouse_id BIGINT NOT NULL,
    supplier_id BIGINT,
    status VARCHAR(32) NOT NULL COMMENT 'DRAFT/PENDING/REJECTED/WAITING_IN/COMPLETED/CANCELLED',
    remark VARCHAR(500),
    submitted_at DATETIME,
    audited_by BIGINT,
    audited_at DATETIME,
    audit_remark VARCHAR(500),
    completed_at DATETIME,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_status (status),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单';

CREATE TABLE inbound_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    inbound_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    quantity DECIMAL(18,4) NOT NULL,
    unit_price DECIMAL(18,4),
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_order_id (inbound_order_id),
    KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单明细';

CREATE TABLE outbound_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    outbound_type VARCHAR(32) NOT NULL COMMENT 'SALE/REQUISITION/TRANSFER/OTHER',
    warehouse_id BIGINT NOT NULL,
    customer_id BIGINT,
    status VARCHAR(32) NOT NULL COMMENT 'DRAFT/PENDING/REJECTED/LOCKED/COMPLETED/CANCELLED',
    remark VARCHAR(500),
    submitted_at DATETIME,
    audited_by BIGINT,
    audited_at DATETIME,
    audit_remark VARCHAR(500),
    completed_at DATETIME,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_status (status),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单';

CREATE TABLE outbound_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    outbound_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    quantity DECIMAL(18,4) NOT NULL,
    unit_price DECIMAL(18,4),
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_order_id (outbound_order_id),
    KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单明细';

CREATE TABLE transfer_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    transfer_type VARCHAR(32) NOT NULL COMMENT 'WAREHOUSE/LOCATION',
    status VARCHAR(32) NOT NULL COMMENT 'DRAFT/PENDING/REJECTED/APPROVED/COMPLETED/CANCELLED',
    remark VARCHAR(500),
    submitted_at DATETIME,
    audited_by BIGINT,
    audited_at DATETIME,
    audit_remark VARCHAR(500),
    completed_at DATETIME,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_status (status),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调拨单';

CREATE TABLE transfer_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transfer_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    from_warehouse_id BIGINT NOT NULL,
    from_area_id BIGINT NOT NULL,
    from_location_id BIGINT NOT NULL,
    to_warehouse_id BIGINT NOT NULL,
    to_area_id BIGINT NOT NULL,
    to_location_id BIGINT NOT NULL,
    quantity DECIMAL(18,4) NOT NULL,
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_order_id (transfer_order_id),
    KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调拨单明细';

CREATE TABLE inventory_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    warehouse_id BIGINT NOT NULL,
    area_id BIGINT,
    location_id BIGINT,
    status VARCHAR(32) NOT NULL COMMENT 'DRAFT/PENDING/REJECTED/APPROVED/ADJUSTED/CANCELLED',
    remark VARCHAR(500),
    submitted_at DATETIME,
    audited_by BIGINT,
    audited_at DATETIME,
    audit_remark VARCHAR(500),
    adjusted_at DATETIME,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_status (status),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点单';

CREATE TABLE inventory_check_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    inventory_check_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    book_quantity DECIMAL(18,4) NOT NULL,
    actual_quantity DECIMAL(18,4),
    difference_quantity DECIMAL(18,4),
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_check_id (inventory_check_id),
    KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点明细';

CREATE TABLE stock_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    log_no VARCHAR(64) NOT NULL UNIQUE,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    change_type VARCHAR(32) NOT NULL COMMENT 'INBOUND/OUTBOUND/TRANSFER/CHECK_ADJUST/MANUAL_ADJUST',
    before_quantity DECIMAL(18,4) NOT NULL,
    change_quantity DECIMAL(18,4) NOT NULL,
    after_quantity DECIMAL(18,4) NOT NULL,
    business_type VARCHAR(64),
    business_id BIGINT,
    business_no VARCHAR(64),
    operated_by BIGINT,
    operated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500),
    KEY idx_product_warehouse (product_id, warehouse_id),
    KEY idx_business (business_type, business_id),
    KEY idx_operated_at (operated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存流水';

CREATE TABLE operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    operator_id BIGINT,
    operator_name VARCHAR(64),
    module_name VARCHAR(64) NOT NULL,
    operation_type VARCHAR(64) NOT NULL,
    operation_content TEXT,
    request_uri VARCHAR(255),
    request_method VARCHAR(16),
    ip_address VARCHAR(64),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_operator_id (operator_id),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';
