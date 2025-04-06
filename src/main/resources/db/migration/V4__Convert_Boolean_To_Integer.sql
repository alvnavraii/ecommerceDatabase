-- Convertir campos booleanos a INTEGER en la tabla users
ALTER TABLE users ALTER COLUMN active TYPE INTEGER USING CASE WHEN active = true THEN 1 ELSE 0 END;
ALTER TABLE users ALTER COLUMN admin TYPE INTEGER USING CASE WHEN admin = true THEN 1 ELSE 0 END;

-- Convertir campos booleanos a INTEGER en la tabla languages
ALTER TABLE languages ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;

-- Convertir campos booleanos en las demás tablas
ALTER TABLE countries ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE autonomous_communities ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE provinces ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE municipalities ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE categories ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE category_translations ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE measurement_units ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE measurement_unit_translations ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE addresses ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
ALTER TABLE addresses ALTER COLUMN is_default TYPE INTEGER USING CASE WHEN is_default = true THEN 1 ELSE 0 END;

-- Convertir otros campos booleanos a INTEGER si existen en otras tablas
-- Por ejemplo:
-- ALTER TABLE autonomous_communities ALTER COLUMN is_active TYPE INTEGER USING CASE WHEN is_active = true THEN 1 ELSE 0 END;
-- etc. 