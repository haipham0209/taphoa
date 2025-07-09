-- Xóa bảng theo thứ tự tránh lỗi khóa ngoại
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS discounts;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS daily_revenue;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS users;

-- Bảng người dùng
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER',
    status ENUM('PENDING', 'ACTIVE', 'DELETED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Token xác thực
CREATE TABLE refresh_tokens (
    token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    revoked TINYINT(1) NOT NULL DEFAULT 0,
    expiry_date DATETIME(6) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Danh mục sản phẩm
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sản phẩm
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    cost_price DECIMAL(10,2) NOT NULL,
    discounted_price DECIMAL(10,2),
    description TEXT,
    stock_quantity INT NOT NULL,
    barcode VARCHAR(13) NOT NULL UNIQUE,
    product_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE
);

-- Giảm giá
CREATE TABLE discounts (
    discount_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    discount_value DECIMAL(5,2) NOT NULL,
    start_date DATETIME,
    end_date DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- Đơn hàng
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id INT,
    total_price DECIMAL(10,2) NOT NULL,
    discount INT DEFAULT 0,
    received_amount DECIMAL(10,2),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'completed', 'canceled') DEFAULT 'pending',
    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Chi tiết đơn hàng
CREATE TABLE order_details (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL,
    product_id INT NOT NULL,
    item_price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_number) REFERENCES orders(order_number) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

-- Doanh thu theo ngày
CREATE TABLE daily_revenue (
    revenue_date DATE NOT NULL PRIMARY KEY,
    total_revenue DECIMAL(10,2) DEFAULT 0,
    total_cost DECIMAL(10,2) DEFAULT 0,
    total_profit DECIMAL(10,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$

CREATE TRIGGER trg_users_role_uppercase_before_insert
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
  SET NEW.role = UPPER(NEW.role);
  SET NEW.status = UPPER(NEW.status);
END $$

CREATE TRIGGER trg_users_role_uppercase_before_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
  SET NEW.role = UPPER(NEW.role);
  SET NEW.status = UPPER(NEW.status);
END $$

DELIMITER ;


-- Tạo tài khoản admin
INSERT INTO users (user_name, email, password, role, status)
VALUES ('admin', 'admin@example.com', '$2a$10$ZoIo8.SG7cUMEARBdVcs4uibCJFFbMvIIlwyMEyVzpWamNqhJ78Wq', 'admin', 'ACTIVE'),
('hai', 'hai@example.com', '$2a$10$ZoIo8.SG7cUMEARBdVcs4uibCJFFbMvIIlwyMEyVzpWamNqhJ78Wq', 'ADMIN', 'PENDING'),
('ngan', 'ngan@example.com', '$2a$10$ZoIo8.SG7cUMEARBdVcs4uibCJFFbMvIIlwyMEyVzpWamNqhJ78Wq', 'ADMIN', 'deleted');

INSERT INTO users (user_name, email, password, status)
VALUES ('cus', 'cus@example.com', '$2a$10$ZoIo8.SG7cUMEARBdVcs4uibCJFFbMvIIlwyMEyVzpWamNqhJ78Wq', 'ACTIVE');



-- UPDATE users SET role = UPPER(role);


