-- !immer CHARACTER SET utf8 COLLATE utf8_unicode_ci, weil COLLATE ...general... die diakritischen Zeichen entfernt und damit nicht korrekt sortiert

create table mathe_jung_alt.MEDIEN(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	SORTNR bigint(20) NOT NULL COMMENT 'für pagination',
	ART varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Art des Mediums - enum',
	TITEL varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Name des Buchs oder der Zeitschrift für  Quellenangaben',
	DESKRIPTOREN varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'kommaseparierte IDs von Deskriptoren aufsteigend sortiert',
	PFAD varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'relativer Pfad auf device, z.B. /mathe/... zeigt auf /media/veracrypt2/mathe/...',
	KOMMENTAR varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_medien_1 (SORTNR),
  	UNIQUE KEY uk_medien_2 (TITEL),
  	UNIQUE KEY uk_medien_3 (PFAD)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

