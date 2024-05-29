CREATE TABLE IF NOT EXISTS Estacion (
  id INT AUTO_INCREMENT PRIMARY KEY,
  direction VARCHAR(255) NOT NULL,
  latitude DECIMAL(9,6) NOT NULL,
  longitude DECIMAL(9,6) NOT NULL
);

DELETE FROM Estacion;

INSERT INTO Estacion VALUES (1, 'Calle de la Estación, 1', 400.4165, -30.70256);
INSERT INTO Estacion VALUES (2, 'Calle de la Estación, 2', 40.4165, -32.70256);
INSERT INTO Estacion VALUES (3, 'Calle de la Estación, 3', 15.4165, -3.70256);
INSERT INTO Estacion VALUES (4, 'Calle de la Estación, 4', 440.4165, 23.70256);
INSERT INTO Estacion VALUES (5, 'Calle de la Estación, 5', 240.4165, 83.70256);