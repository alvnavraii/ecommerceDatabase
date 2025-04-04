-- Tabla de países
CREATE TABLE COUNTRIES (
    ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    CODE VARCHAR2(2) NOT NULL,
    NAME VARCHAR2(100) NOT NULL,
    IS_ACTIVE NUMBER(1) DEFAULT 1 NOT NULL,
    CREATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CREATED_BY VARCHAR2(50) NOT NULL,
    UPDATED_BY VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_COUNTRIES PRIMARY KEY (ID),
    CONSTRAINT UK_COUNTRIES_CODE UNIQUE (CODE)
);

-- Tabla de comunidades autónomas
CREATE TABLE AUTONOMOUS_COMMUNITIES (
    ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    COUNTRY_ID NUMBER NOT NULL,
    CODE VARCHAR2(4) NOT NULL,
    NAME VARCHAR2(100) NOT NULL,
    IS_ACTIVE NUMBER(1) DEFAULT 1 NOT NULL,
    CREATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CREATED_BY VARCHAR2(50) NOT NULL,
    UPDATED_BY VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_AUTONOMOUS_COMMUNITIES PRIMARY KEY (ID),
    CONSTRAINT FK_AC_COUNTRY FOREIGN KEY (COUNTRY_ID) REFERENCES COUNTRIES(ID),
    CONSTRAINT UK_AC_CODE UNIQUE (CODE)
);

-- Tabla de provincias
CREATE TABLE PROVINCES (
    ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    AUTONOMOUS_COMMUNITY_ID NUMBER NOT NULL,
    CODE VARCHAR2(2) NOT NULL,
    NAME VARCHAR2(100) NOT NULL,
    IS_ACTIVE NUMBER(1) DEFAULT 1 NOT NULL,
    CREATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CREATED_BY VARCHAR2(50) NOT NULL,
    UPDATED_BY VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_PROVINCES PRIMARY KEY (ID),
    CONSTRAINT FK_PROVINCES_AC FOREIGN KEY (AUTONOMOUS_COMMUNITY_ID) REFERENCES AUTONOMOUS_COMMUNITIES(ID),
    CONSTRAINT UK_PROVINCES_CODE UNIQUE (CODE)
);

-- Tabla de municipios
CREATE TABLE MUNICIPALITIES (
    ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    PROVINCE_ID NUMBER NOT NULL,
    CODE VARCHAR2(5) NOT NULL,
    NAME VARCHAR2(100) NOT NULL,
    IS_ACTIVE NUMBER(1) DEFAULT 1 NOT NULL,
    CREATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATED_AT TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CREATED_BY VARCHAR2(50) NOT NULL,
    UPDATED_BY VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_MUNICIPALITIES PRIMARY KEY (ID),
    CONSTRAINT FK_MUNICIPALITIES_PROVINCE FOREIGN KEY (PROVINCE_ID) REFERENCES PROVINCES(ID),
    CONSTRAINT UK_MUNICIPALITIES_CODE UNIQUE (CODE)
);

-- Modificar la tabla de direcciones
ALTER TABLE ADDRESSES
DROP COLUMN ADDRESS_LINE1
DROP COLUMN ADDRESS_LINE2
DROP COLUMN CITY
DROP COLUMN STATE
DROP COLUMN COUNTRY;

ALTER TABLE ADDRESSES
ADD ADDRESS CLOB NOT NULL
ADD MUNICIPALITY_ID NUMBER NOT NULL
ADD POSTAL_CODE VARCHAR2(5) NOT NULL
ADD CONSTRAINT FK_ADDRESSES_MUNICIPALITY FOREIGN KEY (MUNICIPALITY_ID) REFERENCES MUNICIPALITIES(ID);

-- Datos de ejemplo para España
INSERT INTO COUNTRIES (CODE, NAME, CREATED_BY, UPDATED_BY)
VALUES ('ES', 'España', 'SYSTEM', 'SYSTEM');

-- Comunidad Autónoma de Madrid
INSERT INTO AUTONOMOUS_COMMUNITIES (COUNTRY_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT ID, 'MAD', 'Comunidad de Madrid', 'SYSTEM', 'SYSTEM'
FROM COUNTRIES WHERE CODE = 'ES';

-- Provincia de Madrid
INSERT INTO PROVINCES (AUTONOMOUS_COMMUNITY_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT ID, '28', 'Madrid', 'SYSTEM', 'SYSTEM'
FROM AUTONOMOUS_COMMUNITIES WHERE CODE = 'MAD';

-- Algunos municipios de Madrid
INSERT INTO MUNICIPALITIES (PROVINCE_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT p.ID, '28079', 'Madrid', 'SYSTEM', 'SYSTEM'
FROM PROVINCES p WHERE p.CODE = '28';

INSERT INTO MUNICIPALITIES (PROVINCE_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT p.ID, '28006', 'Alcalá de Henares', 'SYSTEM', 'SYSTEM'
FROM PROVINCES p WHERE p.CODE = '28';

-- Comunidad Autónoma de Cataluña
INSERT INTO AUTONOMOUS_COMMUNITIES (COUNTRY_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT ID, 'CAT', 'Cataluña', 'SYSTEM', 'SYSTEM'
FROM COUNTRIES WHERE CODE = 'ES';

-- Provincia de Barcelona
INSERT INTO PROVINCES (AUTONOMOUS_COMMUNITY_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT ID, '08', 'Barcelona', 'SYSTEM', 'SYSTEM'
FROM AUTONOMOUS_COMMUNITIES WHERE CODE = 'CAT';

-- Algunos municipios de Barcelona
INSERT INTO MUNICIPALITIES (PROVINCE_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT p.ID, '08019', 'Barcelona', 'SYSTEM', 'SYSTEM'
FROM PROVINCES p WHERE p.CODE = '08';

INSERT INTO MUNICIPALITIES (PROVINCE_ID, CODE, NAME, CREATED_BY, UPDATED_BY)
SELECT p.ID, '08101', 'Hospitalet de Llobregat', 'SYSTEM', 'SYSTEM'
FROM PROVINCES p WHERE p.CODE = '08';

COMMIT;
