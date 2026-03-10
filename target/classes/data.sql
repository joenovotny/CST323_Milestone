INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO products (name, description, price, location)
VALUES ('Sample Product', 'Demo item', 19.99, 'A1');

INSERT INTO customers (first_name, last_name, email, phone, address)
VALUES ('John', 'Smith', 'john@example.com', '555-1234', '123 Main St');