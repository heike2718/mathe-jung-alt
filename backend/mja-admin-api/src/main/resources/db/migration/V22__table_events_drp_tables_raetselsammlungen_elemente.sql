DROP TABLE mathe_jung_alt.RAETSELSAMMLUNGEN;
DROP TABLE mathe_jung_alt.EVENTS;

create table mathe_jung_alt.RAETSELSAMMLUNGEN(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	NAME varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Name der Sammlung',
	SCHWIERIGKEITSGRAD varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'mapped die enum Schwierigkeitsgrad',
	REFERENZTYP varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Typ der semantischen Referenz entsprechend enum Referenztyp',
	REFERENZ varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'ID der Referenz, z.B. mk_wettbewerbe.WETTBEWERBE oder matheag.serien',
	STATUS varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'ERFASST' NOT NULL COMMENT 'Status der Sammlung entsprechend Enum DomainEntityStatus',
	KOMMENTAR varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  	GEAENDERT_AM timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	GEAENDERT_DURCH varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID eines ADMINS',
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_raetselsammlungen_1 (NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

create table mathe_jung_alt.RAETSELSAMMLUNGSELEMENTE(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	NUMMER varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Dient gleichzeitig als Überschrift des Elements in der Sammlung. Die Nummern müssen sortierbar sein, um eine feste Reihenfolge sicherzustellen',
	RAETSEL varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID des Rätsels',
	SAMMLUNG varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID der Rästelsammlung',
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_raetselsammlungselemente_1 (NUMMER, RAETSEL, SAMMLUNG),
	CONSTRAINT `fk_elemente_raetsel` FOREIGN KEY (`RAETSEL`) REFERENCES `RAETSEL` (`UUID`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_elemente_sammlung` FOREIGN KEY (`SAMMLUNG`) REFERENCES `RAETSELSAMMLUNGEN` (`UUID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;





