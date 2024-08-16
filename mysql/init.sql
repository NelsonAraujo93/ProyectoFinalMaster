-- Create Users database if it doesn't exist
CREATE DATABASE IF NOT EXISTS Users;

-- Use the Users database
USE Users;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    dni VARCHAR(255) NOT NULL,
    postal_code INT NOT NULL,
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

-- Create pyme table
CREATE TABLE IF NOT EXISTS pyme (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pyme_postal_code VARCHAR(255) NOT NULL,
    pyme_phone VARCHAR(255) NOT NULL,
    pyme_name VARCHAR(255) NOT NULL,
    pyme_description TEXT,
    user_id INT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- Ensure that user_id is unique to enforce one-to-one relationship
CREATE UNIQUE INDEX idx_user_id ON pyme(user_id);

-- Create service table with reference to pyme table in Users database
CREATE TABLE IF NOT EXISTS services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL,
    pyme_id INT NOT NULL,
    total_rating DOUBLE NOT NULL DEFAULT 0,
    rating_count INT NOT NULL DEFAULT 0,
    average_rating DOUBLE NOT NULL DEFAULT 0,
    FOREIGN KEY (pyme_id) REFERENCES Users.pyme(id) ON DELETE CASCADE
);

-- Clean up existing data
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM blacklisted_tokens;
DELETE FROM pyme;
DELETE FROM services;