-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    dni VARCHAR(255) NOT NULL,
    postal_code INT NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create user_roles table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role)
);

-- Create blacklisted_tokens table
CREATE TABLE IF NOT EXISTS blacklisted_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(512) NOT NULL
);

-- Create pyme table
CREATE TABLE IF NOT EXISTS pyme (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pyme_postal_code VARCHAR(255) NOT NULL,
    pyme_phone VARCHAR(255) NOT NULL,
    pyme_name VARCHAR(255) NOT NULL,
    pyme_description TEXT,
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create services table
CREATE TABLE IF NOT EXISTS services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL,
    pyme_id BIGINT NOT NULL,
    total_rating DOUBLE NOT NULL DEFAULT 0,
    rating_count INT NOT NULL DEFAULT 0,
    average_rating DOUBLE NOT NULL DEFAULT 0,
    FOREIGN KEY (pyme_id) REFERENCES pyme(id) ON DELETE CASCADE
);
