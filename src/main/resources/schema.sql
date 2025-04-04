-- Primero los DROP en orden inverso
DROP TABLE IF EXISTS order_item_translations CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS wishlist_items CASCADE;
DROP TABLE IF EXISTS wishlists CASCADE;
DROP TABLE IF EXISTS product_translations CASCADE;
DROP TABLE IF EXISTS product_images CASCADE;
DROP TABLE IF EXISTS product_categories CASCADE;
DROP TABLE IF EXISTS related_products CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS addresses CASCADE;
DROP TABLE IF EXISTS measurement_unit_translations CASCADE;
DROP TABLE IF EXISTS category_translations CASCADE;
DROP TABLE IF EXISTS measurement_units CASCADE;
DROP TABLE IF EXISTS languages CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS municipalities CASCADE;
DROP TABLE IF EXISTS provinces CASCADE;
DROP TABLE IF EXISTS autonomous_communities CASCADE;
DROP TABLE IF EXISTS countries CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Drop sequences
DROP SEQUENCE IF EXISTS cart_seq;
DROP SEQUENCE IF EXISTS cart_item_seq;
DROP SEQUENCE IF EXISTS wishlist_seq;
DROP SEQUENCE IF EXISTS wishlist_item_seq;
DROP SEQUENCE IF EXISTS product_seq;
DROP SEQUENCE IF EXISTS address_seq;
DROP SEQUENCE IF EXISTS measurement_unit_translation_seq;
DROP SEQUENCE IF EXISTS category_translation_seq;
DROP SEQUENCE IF EXISTS measurement_unit_seq;
DROP SEQUENCE IF EXISTS language_seq;
DROP SEQUENCE IF EXISTS category_seq;
DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS municipality_seq;
DROP SEQUENCE IF EXISTS province_seq;
DROP SEQUENCE IF EXISTS aut_com_seq;
DROP SEQUENCE IF EXISTS country_seq;

-- Create sequences
CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS country_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS cart_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS cart_item_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS wishlist_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS wishlist_item_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS address_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS measurement_unit_translation_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS category_translation_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS measurement_unit_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS language_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS municipality_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS province_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS aut_com_seq START WITH 1 INCREMENT BY 1;

-- 1. Tablas base (sin foreign keys)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY DEFAULT nextval('user_seq'),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    avatar_url VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT true,
    admin BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    CONSTRAINT uk_user_email UNIQUE (email)
);

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

CREATE TABLE IF NOT EXISTS measurement_units (
    id BIGINT PRIMARY KEY DEFAULT nextval('measurement_unit_seq'),
    code VARCHAR(10) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    CONSTRAINT uk_measurement_unit_code UNIQUE (code)
);

-- 2. Products
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY DEFAULT nextval('product_seq'),
    sku VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    measurement_unit_id BIGINT NOT NULL,
    weight DECIMAL(10,2),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    CONSTRAINT uk_product_sku UNIQUE (sku),
    CONSTRAINT fk_product_measurement_unit FOREIGN KEY (measurement_unit_id) REFERENCES measurement_units(id)
);

-- 3. Carts y Cart Items
CREATE TABLE IF NOT EXISTS carts (
    id BIGINT PRIMARY KEY DEFAULT nextval('cart_seq'),
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT PRIMARY KEY DEFAULT nextval('cart_item_seq'),
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    updated_by VARCHAR(50) NOT NULL,
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES carts(id),
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uk_cart_item UNIQUE (cart_id, product_id)
);

-- ... resto de tablas ...

-- Insertar usuario admin con password: Temporal1!
INSERT INTO users (
    id,
    email,
    password,
    first_name,
    last_name,
    phone,
    avatar_url,
    active,
    admin,
    created_at,
    updated_at,
    created_by,
    updated_by
) VALUES (
    nextval('user_seq'),
    'alvnavra@gmail.com',
    '$2a$10$SIOmNmDHMDpFIzXw2cTjK.0RbfZRh7z7u0vCtZijpbWev/7P1vhCa',
    'Rafael',
    'Álvarez Navarrete',
    '',
    '',
    true,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'SYSTEM',
    'SYSTEM'
) ON CONFLICT DO NOTHING; 

-- Insertar idiomas básicos
INSERT INTO languages (id, code, name, is_active, created_at, updated_at, created_by, updated_by)
VALUES 
    (nextval('language_seq'), 'ES', 'Español', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    (nextval('language_seq'), 'EN', 'English', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM')
ON CONFLICT (code) DO NOTHING;

SELECT 
    email,
    CASE WHEN active THEN 'true' ELSE 'false' END as active,
    CASE WHEN admin THEN 'true' ELSE 'false' END as admin
FROM users; 