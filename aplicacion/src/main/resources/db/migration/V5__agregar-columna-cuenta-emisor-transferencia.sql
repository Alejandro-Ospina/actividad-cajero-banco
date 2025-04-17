ALTER TABLE transferencia
ADD COLUMN cuenta_emisor VARCHAR(50);

UPDATE transferencia SET cuenta_emisor = 22222;