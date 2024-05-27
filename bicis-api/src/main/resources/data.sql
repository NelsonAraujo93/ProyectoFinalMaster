-- Create table for aparcamientos
CREATE TABLE aparcamiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(255) NOT NULL,
    max_bicis INT NOT NULL,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL
);

-- Insert some test data
INSERT INTO aparcamiento (direccion, max_bicis, latitud, longitud) VALUES
('Calle 1', 10, 39.4699, -0.3763),
('Calle 2', 20, 40.4168, -3.7038),
('Calle 3', 15, 41.3879, 2.1699);
