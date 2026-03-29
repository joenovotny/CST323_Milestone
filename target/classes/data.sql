INSERT INTO users (username, password, role)
SELECT 'admin', 'admin123', 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

INSERT INTO products (name, description, price, location)
SELECT 'Sample Product', 'Demo item', 19.99, 'A1'
WHERE NOT EXISTS (
    SELECT 1 FROM products WHERE name = 'Sample Product'
);

INSERT INTO customers (first_name, last_name, email, phone, address)
SELECT 'John', 'Smith', 'john@example.com', '555-1234', '123 Main St'
WHERE NOT EXISTS (
    SELECT 1 FROM customers WHERE email = 'john@example.com'
);