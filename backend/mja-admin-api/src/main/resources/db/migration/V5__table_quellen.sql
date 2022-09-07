-- !immer CHARACTER SET utf8 COLLATE utf8_unicode_ci, weil COLLATE ...general... die diakritischen Zeichen entfernt und damit nicht korrekt sortiert

create table mathe_jung_alt.QUELLEN(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	SORTNR bigint(20) NOT NULL COMMENT 'für pagination',
	ART varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Art der Quelle - enum',
	MEDIUM varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Referenz auf MEDIEN.UUID',
	AUSGABE varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'bei Zeitschriften die Nmmmer der Ausgabe',
	JAHRGANG varchar(4) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'bei Zeitschriften das Erscheinungsjahr',
	SEITE varchar(4) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'bei Büchern oder Zeitschriften die Seite',
	PERSON varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Name und möglicherweise Alter und Klassenstufe der Person',
	DESKRIPTOREN varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'kommaseparierte IDs von Deskriptoren aufsteigend sortiert',
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_quellen_1 (SORTNR),
	CONSTRAINT `fk_quellen_medien` FOREIGN KEY (`MEDIUM`) REFERENCES `MEDIEN` (`UUID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

