-- !immer CHARACTER SET utf8 COLLATE utf8_unicode_ci, weil COLLATE ...general... die diakritischen Zeichen entfernt und damit nicht korrekt sortiert

create table mathe_jung_alt.RAETSEL(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	SCHLUESSEL varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'fachlicher Schlüssel und Dateiname',
	TITEL varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Name des Rätsels',
	QUELLE varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Referenz auf QUELLEN',
	DESKRIPTOREN varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'kommaseparierte IDs von Deskriptoren aufsteigend sortiert',
	KOMMENTAR varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
	FRAGE TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'LaTeX- Text der Rätselfrage',
	LOESUNG TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'LaTeX- Text der ausführlichen Lösung',
	ANTWORTVORSCHLAEGE TEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'JSON- Array mit {buchstabe: string, text: string, korrekt: boolean}' CHECK (json_valid(`ANTWORTVORSCHLAEGE`)),
	ANZAHL_ANTWORTEN tinyint DEFAULT 0 COMMENT 'Anzahl der Antwortvorschläge zur Berechnung von Strafpunkten',
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_raetsel_1 (SCHLUESSEL),
	CONSTRAINT `fk_raetsel_quellen` FOREIGN KEY (`QUELLE`) REFERENCES `QUELLEN` (`UUID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

