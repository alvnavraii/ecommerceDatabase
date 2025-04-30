/**
 * Script para migrar esquema y datos desde PostgreSQL a SQLite
 * 
 * Este script extrae:
 * - Estructura de tablas
 * - Índices
 * - Triggers
 * - Datos
 * - Secuencias
 * 
 * Y genera el script SQL equivalente para SQLite
 */

// Dependencias necesarias (debes instalarlas con npm)
// npm install pg better-sqlite3 dotenv

require('dotenv').config();
const { Client } = require('pg');
const fs = require('fs');
const path = require('path');

// Configuración de PostgreSQL
const pgConfig = {
  user: process.env.PG_USER || 'postgres',
  host: process.env.PG_HOST || 'localhost',
  database: process.env.PG_DATABASE || 'ecommerce',
  password: process.env.PG_PASSWORD || 'postgres',
  port: process.env.PG_PORT || 5433,
};

// Ruta donde se guardará el script SQLite
const outputPath = process.env.OUTPUT_PATH || './sqlite_migration.sql';

// Mapeo de tipos de datos de PostgreSQL a SQLite
const typeMapping = {
  'bigint': 'INTEGER',
  'bigserial': 'INTEGER',
  'boolean': 'INTEGER',
  'character varying': 'TEXT',
  'character': 'TEXT',
  'date': 'TEXT',
  'double precision': 'REAL',
  'integer': 'INTEGER',
  'numeric': 'REAL',
  'real': 'REAL',
  'smallint': 'INTEGER',
  'serial': 'INTEGER',
  'text': 'TEXT',
  'timestamp with time zone': 'TIMESTAMP',
  'timestamp without time zone': 'TIMESTAMP',
  'uuid': 'TEXT',
  'jsonb': 'TEXT',
  'json': 'TEXT'
};

// Función para escapar valores para SQLite
function escapeSQLite(value) {
  if (value === null || value === undefined) return 'NULL';
  if (typeof value === 'boolean') return value ? '1' : '0';
  if (typeof value === 'number') return value;
  if (typeof value === 'string') return `'${value.replace(/'/g, "''")}'`;
  return `'${value}'`;
}

// Función para convertir un tipo PostgreSQL a SQLite
function convertType(pgType) {
  for (const [pg, sqlite] of Object.entries(typeMapping)) {
    if (pgType.startsWith(pg)) {
      return sqlite;
    }
  }
  console.warn(`Tipo no mapeado: ${pgType}, usando TEXT como fallback`);
  return 'TEXT';
}

// Función para escapar identificadores
function escapeIdentifier(identifier) {
  return `"${identifier.replace(/"/g, '""')}"`;
}

// Función principal
async function migrateToSQLite() {
  const client = new Client(pgConfig);
  let sqliteScript = '';
  
  try {
    console.log('Conectando a PostgreSQL...');
    await client.connect();
    console.log('Conexión establecida.');
    
    // Inicio de transacción para SQLite
    sqliteScript += '-- Script de migración PostgreSQL a SQLite\n';
    sqliteScript += `-- Generado el ${new Date().toISOString()}\n`;
    sqliteScript += 'BEGIN TRANSACTION;\n\n';
    
    // 1. Obtener todas las tablas en el esquema público
    console.log('Obteniendo listado de tablas...');
    const tablesRes = await client.query(`
      SELECT table_name
      FROM information_schema.tables
      WHERE table_schema = 'public'
        AND table_type = 'BASE TABLE';
    `);
    
    const tables = tablesRes.rows.map(row => row.table_name);
    console.log(`Se encontraron ${tables.length} tablas.`);
    
    // 2. Para cada tabla, generar su esquema en SQLite
    for (const tableName of tables) {
      console.log(`Procesando tabla: ${tableName}`);
      
      // Obtener información de columnas
      const columnsRes = await client.query(`
        SELECT 
          column_name, 
          data_type, 
          column_default, 
          is_nullable,
          character_maximum_length
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = $1
        ORDER BY ordinal_position;
      `, [tableName]);
      
      // Obtener restricciones de clave primaria
      const pkRes = await client.query(`
        SELECT
          kcu.column_name
        FROM information_schema.table_constraints tc
        JOIN information_schema.key_column_usage kcu
          ON tc.constraint_name = kcu.constraint_name
          AND tc.table_schema = kcu.table_schema
        WHERE tc.constraint_type = 'PRIMARY KEY'
          AND tc.table_schema = 'public'
          AND tc.table_name = $1
        ORDER BY kcu.ordinal_position;
      `, [tableName]);
      
      const primaryKeys = pkRes.rows.map(row => row.column_name);
      
      // Obtener restricciones de clave foránea
      const fkRes = await client.query(`
        SELECT
          kcu.column_name,
          ccu.table_name AS foreign_table_name,
          ccu.column_name AS foreign_column_name
        FROM information_schema.table_constraints tc
        JOIN information_schema.key_column_usage kcu
          ON tc.constraint_name = kcu.constraint_name
          AND tc.table_schema = kcu.table_schema
        JOIN information_schema.constraint_column_usage ccu
          ON ccu.constraint_name = tc.constraint_name
          AND ccu.table_schema = tc.table_schema
        WHERE tc.constraint_type = 'FOREIGN KEY'
          AND tc.table_schema = 'public'
          AND tc.table_name = $1;
      `, [tableName]);
      
      const foreignKeys = fkRes.rows;
      
      // Generar script CREATE TABLE
      sqliteScript += `-- Crear tabla ${tableName}\n`;
      sqliteScript += `CREATE TABLE ${tableName} (\n`;
      
      const columnDefs = columnsRes.rows.map(col => {
        const isAutoIncrement = col.column_default && col.column_default.includes('nextval');
        const sqliteType = convertType(col.data_type);
        let columnDef = `  ${col.column_name} ${sqliteType}`;
        
        // Gestionar nulabilidad
        if (col.is_nullable === 'NO') {
          columnDef += ' NOT NULL';
        }
        
        // Gestionar valores por defecto (excepto autoincrement que se maneja con la PK)
        if (col.column_default && !isAutoIncrement) {
          let defaultValue = col.column_default;
          // Convertir valores booleanos
          if (defaultValue === 'true') defaultValue = '1';
          if (defaultValue === 'false') defaultValue = '0';
          
          columnDef += ` DEFAULT ${defaultValue}`;
        }
        
        return columnDef;
      });
      
      // Agregar constraint de clave primaria
      if (primaryKeys.length > 0) {
        columnDefs.push(`  PRIMARY KEY (${primaryKeys.join(', ')})`);
      }
      
      // Agregar constraints de clave foránea
      foreignKeys.forEach(fk => {
        columnDefs.push(`  FOREIGN KEY (${fk.column_name}) REFERENCES ${fk.foreign_table_name}(${fk.foreign_column_name})`);
      });
      
      sqliteScript += columnDefs.join(',\n');
      sqliteScript += '\n);\n\n';
      
      // 3. Obtener y migrar índices
      const indicesRes = await client.query(`
        SELECT
          i.relname AS index_name,
          a.attname AS column_name,
          ix.indisunique AS is_unique
        FROM
          pg_index ix
          JOIN pg_class i ON i.oid = ix.indexrelid
          JOIN pg_class t ON t.oid = ix.indrelid
          JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY(ix.indkey)
          JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE
          t.relkind = 'r'
          AND t.relname = $1
          AND n.nspname = 'public'
          AND i.relname NOT LIKE '%_pkey'
        ORDER BY
          i.relname, a.attnum;
      `, [tableName]);
      
      // Agrupar índices por nombre
      const indices = {};
      indicesRes.rows.forEach(row => {
        if (!indices[row.index_name]) {
          indices[row.index_name] = {
            is_unique: row.is_unique,
            columns: []
          };
        }
        indices[row.index_name].columns.push(row.column_name);
      });
      
      // Generar DDL de índices
      for (const [indexName, index] of Object.entries(indices)) {
        const uniqueStr = index.is_unique ? 'UNIQUE ' : '';
        sqliteScript += `-- Crear índice ${indexName}\n`;
        sqliteScript += `CREATE ${uniqueStr}INDEX IF NOT EXISTS ${indexName} ON ${tableName} (${index.columns.join(', ')});\n\n`;
      }
      
      // 4. Migrar datos
      console.log(`Extrayendo datos de la tabla ${tableName}...`);
      const dataRes = await client.query(`SELECT * FROM ${tableName};`);
      
      if (dataRes.rows.length > 0) {
        sqliteScript += `-- Insertar datos en ${tableName}\n`;
        
        for (const row of dataRes.rows) {
          const columns = Object.keys(row).join(', ');
          const values = Object.values(row).map(escapeSQLite).join(', ');
          sqliteScript += `INSERT INTO ${tableName} (${columns}) VALUES (${values});\n`;
        }
        sqliteScript += '\n';
      }
    }
    
    // 5. Obtener y migrar triggers
    console.log('Extrayendo triggers...');
    const triggersRes = await client.query(`
      SELECT
        t.trigger_name,
        t.event_manipulation,
        t.action_timing,
        t.event_object_table,
        pg_get_triggerdef(pgtr.oid) AS trigger_definition
      FROM information_schema.triggers t
      JOIN pg_trigger pgtr ON t.trigger_name = pgtr.tgname
      WHERE t.trigger_schema = 'public';
    `);
    
    if (triggersRes.rows.length > 0) {
      sqliteScript += '-- Triggers (nota: requieren adaptación manual para SQLite)\n';
      sqliteScript += '-- SQLite usa una sintaxis diferente para los triggers\n\n';
      
      for (const trigger of triggersRes.rows) {
        sqliteScript += `-- Trigger original de PostgreSQL: ${trigger.trigger_name}\n`;
        sqliteScript += `-- ${trigger.trigger_definition}\n`;
        sqliteScript += `-- Tabla: ${trigger.event_object_table}, Evento: ${trigger.action_timing} ${trigger.event_manipulation}\n`;
        sqliteScript += '-- Conversión a SQLite (esqueleto, requiere implementación manual):\n';
        sqliteScript += `-- CREATE TRIGGER ${trigger.trigger_name}\n`;
        sqliteScript += `--   ${trigger.action_timing} ${trigger.event_manipulation} ON ${trigger.event_object_table}\n`;
        sqliteScript += `--   BEGIN\n`;
        sqliteScript += `--     -- Implementar lógica aquí\n`;
        sqliteScript += `--   END;\n\n`;
      }
    }
    
    // 6. Obtener y migrar secuencias
    console.log('Extrayendo secuencias...');
    const sequencesRes = await client.query(`
      SELECT
        sequence_name,
        data_type,
        start_value,
        minimum_value,
        maximum_value,
        increment
      FROM information_schema.sequences
      WHERE sequence_schema = 'public';
    `);
    
    if (sequencesRes.rows.length > 0) {
      sqliteScript += '-- Secuencias (SQLite usa AUTOINCREMENT con PRIMARY KEY)\n';
      sqliteScript += '-- La siguiente información es referencial para ajustar secuencias en SQLite\n\n';
      
      for (const seq of sequencesRes.rows) {
        // Obtener valor actual de la secuencia
        const currentValueRes = await client.query(`SELECT last_value FROM ${seq.sequence_name};`);
        const currentValue = currentValueRes.rows[0].last_value;
        
        sqliteScript += `-- Secuencia original: ${seq.sequence_name}\n`;
        sqliteScript += `-- Tipo: ${seq.data_type}, Valor actual: ${currentValue}\n`;