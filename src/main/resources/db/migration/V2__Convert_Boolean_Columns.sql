-- Crear columnas temporales
ALTER TABLE users ADD COLUMN active_int integer;
ALTER TABLE users ADD COLUMN admin_int integer;

-- Convertir los datos booleanos (PostgreSQL usa 't'/'f') a enteros
UPDATE users SET active_int = CASE WHEN active = true THEN 1 ELSE 0 END;
UPDATE users SET admin_int = CASE WHEN admin = true THEN 1 ELSE 0 END;

-- Eliminar las columnas originales
ALTER TABLE users DROP COLUMN active;
ALTER TABLE users DROP COLUMN admin;

-- Renombrar las columnas nuevas
ALTER TABLE users RENAME COLUMN active_int TO active;
ALTER TABLE users RENAME COLUMN admin_int TO admin;

-- Repetir para otras tablas
-- ...otras tablas... 