CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
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
('admin', '$2a$12$ee0av8DOyESK9cVLlW7TueEX7CwpveCK6udKa9fyrTkOqxb0ckRJu', true),  -- adminpass (hashed)
('estacion', '$2a$12$a2hKShdRdZnJxh5UxflIsuYizILIPTuw72wAa4am/8.JZfe4XXzwe', true),  -- estacionpass (hashed)
('aparcamiento', '$2a$12$7R0ASxT5AugSyMmqTXfaGOkYa8I/dB7vTEhjNZgylgVthrAKBpDda', true),  -- aparcamientopass (hashed)
('servicio', '$2a$12$Sn8Kzyjn0bW536yHK5cBj.J2qIr2nSvd.6U/fSuUDDKgq3O7iu5dm', true);  -- serviciopass (hashed)

-- Insert roles for initial users
INSERT INTO user_roles (user_id, role) VALUES
((SELECT id FROM users WHERE username = 'admin'), 'ADMIN'),
((SELECT id FROM users WHERE username = 'estacion'), 'ESTACION'),
((SELECT id FROM users WHERE username = 'aparcamiento'), 'APARCAMIENTO'),
((SELECT id FROM users WHERE username = 'servicio'), 'SERVICIO');
