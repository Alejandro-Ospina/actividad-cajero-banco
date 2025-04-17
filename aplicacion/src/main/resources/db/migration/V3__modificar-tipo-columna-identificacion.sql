ALTER TABLE usuario
ALTER COLUMN identificacion TYPE VARCHAR(20)
USING identificacion::VARCHAR;