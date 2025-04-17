-- Agregar restricciones en la tabla usuarios
ALTER TABLE usuario
ADD CONSTRAINT uq_nombre_usuario UNIQUE (nombre),
ADD CONSTRAINT uq_identificacion_usuario UNIQUE (identificacion);

-- Agregar restricciones en la tabla cuenta banco
ALTER TABLE cuenta_banco
ADD CONSTRAINT uq_numero_cuenta_cuenta_banco UNIQUE (numero_cuenta);