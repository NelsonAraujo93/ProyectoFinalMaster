-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS Users;

-- Use the database
USE Users;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create user_roles table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role)
);

-- Create blacklisted_tokens table
CREATE TABLE IF NOT EXISTS blacklisted_tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(512) NOT NULL
);

-- Clean up existing data
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM blacklisted_tokens;

-- Insert initial users
INSERT INTO users (username, password, enabled) VALUES
('nelson', '$2a$12$ee0av8DOyESK9cVLlW7TueEX7CwpveCK6udKa9fyrTkOqxb0ckRJu', true),  -- adminpass (hashed)
('pipe', '$2a$12$a2hKShdRdZnJxh5UxflIsuYizILIPTuw72wAa4am/8.JZfe4XXzwe', true),  -- estacionpass (hashed)
('Carlos', '$2a$12$7R0ASxT5AugSyMmqTXfaGOkYa8I/dB7vTEhjNZgylgVthrAKBpDda', true);  -- aparcamientopass (hashed)

-- Insert roles for initial users
INSERT INTO user_roles (user_id, role) VALUES
((SELECT id FROM users WHERE username = 'nelson'), 'ADMIN'),
((SELECT id FROM users WHERE username = 'pipe'), 'CLIENT'),
((SELECT id FROM users WHERE username = 'Carlos'), 'PYME');

-- Create Services database if it doesn't exist
CREATE DATABASE IF NOT EXISTS Services;

-- Use the Services database
USE Services;

-- Create service table
CREATE TABLE IF NOT EXISTS service (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users.users(id)
);
