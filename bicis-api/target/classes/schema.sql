CREATE TABLE IF NOT EXISTS parking (
  id INT AUTO_INCREMENT PRIMARY KEY,
  direction VARCHAR(255) NOT NULL,
  bikes_capacity INT NOT NULL,
  latitude DECIMAL(9,6) NOT NULL,
  longitude DECIMAL(9,6) NOT NULL
);

DELETE FROM parking;

INSERT INTO parking VALUES (1, 'Calle de la Estación, 1', 10, 400.4165, -30.70256);
INSERT INTO parking VALUES (2, 'Calle de la Estación, 2', 200, 40.4165, -32.70256);
INSERT INTO parking VALUES (3, 'Calle de la Estación, 3', 30, 15.4165, -3.70256);
INSERT INTO parking VALUES (4, 'Calle de la Estación, 4', 40, 440.4165, 23.70256);
INSERT INTO parking VALUES (5, 'Calle de la Estación, 5', 50, 240.4165, 3.70256);