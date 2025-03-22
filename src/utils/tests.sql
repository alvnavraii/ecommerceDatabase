DROP TABLE ROLES CASCADE CONSTRAINTS;

ALTER TABLE USERS ADD IS_ADMIN NUMBER(1) DEFAULT 0;

TRUNCATE TABLE AUTONOMOUS_COMMUNITIES;


INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('03', 'Asturias, Principado de', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('04', 'Balears, Illes', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('05', 'Canarias', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('06', 'Cantabria', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('07', 'Castilla y León', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('08', 'Castilla-La Mancha', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('10', 'Ceuta', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('11', 'Comunitat Valenciana', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('12', 'Extremadura', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('13', 'Galicia', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('15', 'Melilla', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('16', 'Murcia, Región de', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('17', 'Navarra, Comunidad Foral de', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('18', 'País Vasco', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

INSERT INTO autonomous_communities (code, name, country_id, is_active, created_at, created_by, updated_at, updated_by)
VALUES ('19', 'Rioja, La', 1, 1, SYSDATE, 'system', SYSDATE, 'system');

alter table autonomous_communities add alfa_code varchar(3);
COMMIT;

select table_name from user_tables
ORDER BY table_name;

desc languages;

desc municipalities;

desc provinces;

alter table provinces rename column autonomous_community_id to autcom_id;

desc autonomous_communities;

desc countries;

select * from countries;

ALTER TABLE ECOMMERCE.category_translations ADD IS_ACTIVE NUMBER(1) DEFAULT 1 NOT NULL;

desc products;

DESC ADDRESSES;

drop table addresses_old CASCADE CONSTRAINTS;

select * from languages order by id;

delete from languages where id = 6;

SELECT * FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'LANGUAGES' and constraint_name in ('SYS_C0012533','SYS_C0012534');

select * from CATEGORY_TRANSLATIONS where language_id = 6;

update category_translations set language_id = 1 where language_id = 6;

select * from users;

select * from countries;

select * from autonomous_communities;

desc autonomous_communities;

alter table autonomous_communities add numeric_code varchar(2);

alter table autonomous_communities drop column numeric_code;

alter table autonomous_communities MODIFY code 
  VARCHAR2(2);

update autonomous_communities set code = '09' where id = 2;
update AUTONOMOUS_COMMUNITIES set code = '14' where id = 1;

commit;

UPDATE autonomous_communities SET alfa_code = 'AND' WHERE code = '01';
UPDATE autonomous_communities SET alfa_code = 'ARA' WHERE code = '02';
UPDATE autonomous_communities SET alfa_code = 'AST' WHERE code = '03';
UPDATE autonomous_communities SET alfa_code = 'BAL' WHERE code = '04';
UPDATE autonomous_communities SET alfa_code = 'CAN' WHERE code = '05';
UPDATE autonomous_communities SET alfa_code = 'CNT' WHERE code = '06';
UPDATE autonomous_communities SET alfa_code = 'CLN' WHERE code = '07';
UPDATE autonomous_communities SET alfa_code = 'CLM' WHERE code = '08';
UPDATE autonomous_communities SET alfa_code = 'CAT' WHERE code = '09';
UPDATE autonomous_communities SET alfa_code = 'CEU' WHERE code = '10';
UPDATE autonomous_communities SET alfa_code = 'VAL' WHERE code = '11';
UPDATE autonomous_communities SET alfa_code = 'EXT' WHERE code = '12';
UPDATE autonomous_communities SET alfa_code = 'GAL' WHERE code = '13';
UPDATE autonomous_communities SET alfa_code = 'MAD' WHERE code = '14';
UPDATE autonomous_communities SET alfa_code = 'MEL' WHERE code = '15';
UPDATE autonomous_communities SET alfa_code = 'MUR' WHERE code = '16';
UPDATE autonomous_communities SET alfa_code = 'NAV' WHERE code = '17';
UPDATE autonomous_communities SET alfa_code = 'VAS' WHERE code = '18';
UPDATE autonomous_communities SET alfa_code = 'RIO' WHERE code = '19';

commit;

select table_name from user_tables where table_name like 'PROV%';

desc provinces;

alter table provinces add alfa_code varchar(3);

select *
  from provinces;

  delete from provinces where id = 22;
  commit;

  desc addresses;

select * from addresses;

select * from users;

select table_name from user_tables where table_name like 'MEASUREMENT%';

desc measurement_units;

select * from measurement_units;

CREATE TABLE measurement_unit_translations (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    measurement_unit_id NUMBER NOT NULL,
    language_id NUMBER NOT NULL,
    name VARCHAR2(50) NOT NULL,
    is_active NUMBER(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    created_by VARCHAR2(50) NOT NULL,
    updated_by VARCHAR2(50) NOT NULL,
    CONSTRAINT fk_mut_measurement_unit FOREIGN KEY (measurement_unit_id) REFERENCES measurement_units(id),
    CONSTRAINT fk_mut_language FOREIGN KEY (language_id) REFERENCES languages(id),
    CONSTRAINT uk_mut_measurement_unit_lang UNIQUE (measurement_unit_id, language_id)
);

insert into measurement_unit_translations (measurement_unit_id, language_id, name, is_active, created_by, updated_by, created_at, updated_at)
values (1, 2, 'Kilogram', 1, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

commit;

select * from measurement_units;

