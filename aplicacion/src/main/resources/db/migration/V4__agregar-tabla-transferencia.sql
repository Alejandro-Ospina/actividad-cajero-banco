-- Crear tabla de transferencias bancarias
CREATE TABLE transferencia
(
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT NOW(),
    emisor VARCHAR(50) NOT NULL,
    identificacion VARCHAR(50) NOT NULL,
    receptor VARCHAR(50) NOT NULL,
    nombre_receptor VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    monto INT NOT NULL,
    cuenta_id INT NOT NULL
);

-- Agregar llave foranea
ALTER TABLE transferencia
ADD CONSTRAINT fk_transferencia_cuenta_banco
FOREIGN KEY (cuenta_id) REFERENCES cuenta_banco (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- Crear indice y mejorar las busquedas por indice
CREATE INDEX idx_transferencia_cuenta_id ON transferencia (cuenta_id);