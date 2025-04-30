-- Script de migración a SQLite
-- Creado el 2025-04-23

-- Crear tabla languages
CREATE TABLE languages (
  id INTEGER PRIMARY KEY,
  code TEXT NOT NULL,
  name TEXT NOT NULL,
  native_name TEXT,
  flag_url TEXT,
  is_active INTEGER NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by TEXT NOT NULL,
  updated_by TEXT NOT NULL
);

-- Crear tabla users
CREATE TABLE users (
  id INTEGER PRIMARY KEY,
  email TEXT NOT NULL,
  password TEXT NOT NULL,
  first_name TEXT,
  last_name TEXT,
  phone TEXT,
  avatar_url TEXT,
  active INTEGER NOT NULL DEFAULT 1,
  admin INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by TEXT NOT NULL,
  updated_by TEXT NOT NULL
);

-- Crear tabla countries
CREATE TABLE countries (
  id INTEGER PRIMARY KEY,
  code TEXT NOT NULL,
  name TEXT NOT NULL,
  is_active INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by TEXT NOT NULL,
  updated_by TEXT NOT NULL
);

-- Crear tabla autonomous_communities
CREATE TABLE autonomous_communities (
  id INTEGER PRIMARY KEY,
  code TEXT NOT NULL,
  alfa_code TEXT NOT NULL,
  name TEXT NOT NULL,
  is_active INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by TEXT NOT NULL,
  updated_by TEXT NOT NULL,
  country_id INTEGER NOT NULL,
  FOREIGN KEY (country_id) REFERENCES countries (id)
);

-- Crear tabla provinces
CREATE TABLE provinces (
  id INTEGER PRIMARY KEY,
  code TEXT NOT NULL,
  alfa_code TEXT NOT NULL,
  name TEXT NOT NULL,
  is_active INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  created_by TEXT NOT NULL,
  updated_by TEXT NOT NULL,
  autcom_id INTEGER NOT NULL,
  FOREIGN KEY (autcom_id) REFERENCES autonomous_communities (id)
);

-- Crear tabla municipalities
CREATE TABLE municipalities (
  