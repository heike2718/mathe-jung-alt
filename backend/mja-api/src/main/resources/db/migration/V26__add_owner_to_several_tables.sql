drop table TEMP_RAETSELGRUPPENELEMENTE;
drop table TEMP_RAETSELGRUPPEN;

ALTER TABLE RAETSEL ADD COLUMN OWNER varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'UUID desjenigen USERS, der den Datensatz angelegt hat';
ALTER TABLE RAETSELGRUPPEN ADD COLUMN OWNER varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'UUID desjenigen USERS, der den Datensatz angelegt hat';
ALTER TABLE QUELLEN ADD COLUMN OWNER varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'UUID desjenigen USERS, der den Datensatz angelegt hat';
ALTER TABLE MEDIEN ADD COLUMN OWNER varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID desjenigen USERS, der den Datensatz angelegt hat';

UPDATE RAETSEL set OWNER = GEAENDERT_DURCH;
UPDATE RAETSELGRUPPEN set OWNER = GEAENDERT_DURCH;
UPDATE QUELLEN set OWNER = GEAENDERT_DURCH;

ALTER TABLE RAETSEL MODIFY OWNER varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID desjenigen USERS, der den Datensatz angelegt hat';
ALTER TABLE RAETSELGRUPPEN MODIFY OWNER varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID desjenigen USERS, der den Datensatz angelegt hat';
ALTER TABLE QUELLEN MODIFY OWNER varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID desjenigen USERS, der den Datensatz angelegt hat';
