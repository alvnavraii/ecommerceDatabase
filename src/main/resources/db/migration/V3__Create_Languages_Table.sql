-- Crear la tabla languages
CREATE TABLE IF NOT EXISTS languages (
    id BIGINT PRIMARY KEY DEFAULT nextval('language_seq'),
    code VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    native_name VARCHAR(100),
    flag_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    CONSTRAINT uk_language_code UNIQUE (code)
);

-- Insertar idiomas básicos
INSERT INTO languages (id, code, name, native_name, flag_url, is_active, created_at, updated_at, created_by, updated_by)
VALUES 
    (nextval('language_seq'), 'ES', 'Español', 'Español', 'https://flagcdn.com/w320/es.png', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    (nextval('language_seq'), 'EN', 'English', 'English', 'https://flagcdn.com/w320/gb.png', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM')
ON CONFLICT (code) DO NOTHING; 