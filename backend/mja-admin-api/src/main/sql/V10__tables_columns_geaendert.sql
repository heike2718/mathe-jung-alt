ALTER TABLE RAETSEL ADD COLUMN GEAENDERT_DURCH varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID eines ADMINS';

ALTER TABLE RAETSEL ADD COLUMN `GEAENDERT_AM` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE MEDIEN ADD COLUMN GEAENDERT_DURCH varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID eines ADMINS';

ALTER TABLE MEDIEN ADD COLUMN `GEAENDERT_AM` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE QUELLEN ADD COLUMN `GEAENDERT_AM` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE QUELLEN ADD COLUMN GEAENDERT_DURCH varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID eines ADMINS';
