-- !immer CHARACTER SET utf8 COLLATE utf8_unicode_ci, weil COLLATE ...general... die diakritischen Zeichen entfernt und damit nicht korrekt sortiert

create table mathe_jung_alt.RAETSELSAMMLUNGEN(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	NAME varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Name der Sammlung',
	DESKRIPTOREN varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'kommaseparierte IDs von Deskriptoren aufsteigend sortiert',
	SEMANTIC_REFERENCE varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Referenz auf mk_wettbewerbe.WETTBEWERBE oder Runde einer Serie',
	KOMMENTAR varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
	INHALT longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Raetselschluessel mit Nummer in der korrekten Reihenfolge' CHECK (json_valid(INHALT)),
  	GEAENDERT_AM timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	GEAENDERT_DURCH varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID eines ADMINS',
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_raetselsammlungen_1 (NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE RAETSELSAMMLUNGEN ADD FULLTEXT INDEX ind_raetselsammlungen_ft (NAME, KOMMENTAR, SEMANTIC_REFERENCE);

