CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role)
);

-- Clean up existing data
DELETE FROM user_roles;
DELETE FROM users;


-- Insert initial users
INSERT INTO users (username, password, enabled) VALUES
('admin', '$2a$10$1234567890123456789012', TRUE),  -- adminpass (hashed)
('estacion', '$2a$10$1234567890123456789012', TRUE),  -- estacionpass (hashed)
('aparcamiento', '$2a$10$1234567890123456789012', TRUE),  -- aparcamientopass (hashed)
('servicio', '$2a$10$1234567890123456789012', TRUE);  -- serviciopass (hashed)

-- Insert roles for initial users
INSERT INTO user_roles (user_id, role) VALUES
((SELECT id FROM users WHERE username = 'admin'), 'ADMIN'),
((SELECT id FROM users WHERE username = 'estacion'), 'ESTACION'),
((SELECT id FROM users WHERE username = 'aparcamiento'), 'APARCAMIENTO'),
((SELECT id FROM users WHERE username = 'servicio'), 'SERVICIO');
