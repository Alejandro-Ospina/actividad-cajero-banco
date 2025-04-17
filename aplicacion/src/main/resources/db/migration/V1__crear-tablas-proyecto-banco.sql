-- crear tabla usuario
CREATE TABLE usuario
(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    identificacion INT NOT NULL,
    edad INT NOT NULL,
    nacionalidad VARCHAR(50) NOT NULL,
    genero VARCHAR(50) NOT NULL
);

-- crear tabla cuentas de banco
CREATE TABLE cuenta_banco
(
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(100) NOT NULL,
    clave VARCHAR(100) NOT NULL,
    nombre_banco VARCHAR(50) NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo INT NOT NULL,
    usuario_id INT NOT NULL
);

-- establecer la relacion de la tabla usuarios con la tabla de cuenta bancaria
ALTER TABLE cuenta_banco
ADD CONSTRAINT fk_cuenta_banco_usuario
FOREIGN KEY (usuario_id) REFERENCES usuario (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- crear indice para la llave foranea de la tabla cuenta banco
CREATE INDEX idx_cuenta_banco_usuario_id ON cuenta_banco (usuario_id);

