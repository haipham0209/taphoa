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
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
	    stock_quantity INT DEFAULT 0,
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

INSERT INTO users (user_name, email, password)
VALUES ('cus', 'cus@example.com', '$2a$10$ZoIo8.SG7cUMEARBdVcs4uibCJFFbMvIIlwyMEyVzpWamNqhJ78Wq');


CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL
);

INSERT INTO category (category_name) VALUES
('Đồ uống'),
('Thực phẩm'),
('Văn phòng phẩm'),
('Điện tử'),
('Đồ chơi'),
('Thời trang');

INSERT INTO product (category_id, product_name, price, cost_price, discounted_price, description, stock_quantity, barcode, product_image, created_at, updated_at) VALUES
-- Đồ uống (category_id = 1)
(1, 'Nước ngọt Coca Cola', 15000, 10000, 14000, 'Nước ngọt vị Cola', 100, '0000000000001', 'cocacola.jpg', NOW(), NOW()),
(1, 'Nước suối Aquafina', 10000, 7000, NULL, 'Nước suối tinh khiết', 200, '0000000000002', 'aquafina.jpg', NOW(), NOW()),
(1, 'Trà xanh không đường', 12000, 8000, 11000, 'Trà xanh thơm ngon', 150, '0000000000003', 'green_tea.jpg', NOW(), NOW()),

-- Thực phẩm (category_id = 2)
(2, 'Bánh mì sandwich', 25000, 15000, 22000, 'Bánh mì tươi ngon', 50, '0000000000004', 'sandwich.jpg', NOW(), NOW()),
(2, 'Phô mai Cheddar', 60000, 40000, 55000, 'Phô mai nhập khẩu', 30, '0000000000005', 'cheddar.jpg', NOW(), NOW()),

-- Văn phòng phẩm (category_id = 3)
(3, 'Bút bi Thiên Long', 5000, 3000, NULL, 'Bút bi chất lượng', 500, '0000000000006', 'pen.jpg', NOW(), NOW()),
(3, 'Tập vở 200 trang', 15000, 10000, 14000, 'Tập vở học sinh', 300, '0000000000007', 'notebook.jpg', NOW(), NOW()),

-- Điện tử (category_id = 4)
(4, 'Tai nghe Bluetooth', 300000, 250000, 280000, 'Tai nghe không dây', 20, '0000000000008', 'headphone.jpg', NOW(), NOW()),
(4, 'Sạc dự phòng 10000mAh', 400000, 350000, NULL, 'Pin sạc dự phòng', 15, '0000000000009', 'powerbank.jpg', NOW(), NOW()),

-- Đồ chơi (category_id = 5)
(5, 'Lego bộ lâu đài', 700000, 600000, 650000, 'Bộ đồ chơi lego', 10, '0000000000010', 'lego_castle.jpg', NOW(), NOW()),
(5, 'Búp bê Barbie', 350000, 300000, NULL, 'Búp bê thời trang', 25, '0000000000011', 'barbie.jpg', NOW(), NOW()),

-- Thời trang (category_id = 6)
(6, 'Áo thun nam', 200000, 150000, 180000, 'Áo thun cotton', 100, '0000000000012', 'shirt.jpg', NOW(), NOW()),
(6, 'Quần jean nữ', 450000, 400000, 420000, 'Quần jean thời trang', 80, '0000000000013', 'jeans.jpg', NOW(), NOW());

INSERT INTO discounts (product_id, discount_value, start_date, end_date, created_at, updated_at) VALUES
(1, 10.00, '2025-07-01 00:00:00', '2025-07-31 23:59:59', NOW(), NOW()),
(3, 8.00, '2025-07-05 00:00:00', '2025-07-20 23:59:59', NOW(), NOW()),
(7, 5.00, '2025-07-10 00:00:00', '2025-07-25 23:59:59', NOW(), NOW()),
(10, 7.50, '2025-07-01 00:00:00', '2025-07-15 23:59:59', NOW(), NOW());


-- UPDATE users SET role = UPPER(role);


