DROP TABLE mathe_jung_alt.RAETSELSAMMLUNGSELEMENTE;
DROP TABLE mathe_jung_alt.RAETSELSAMMLUNGEN;

create table mathe_jung_alt.RAETSELGRUPPEN(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	NAME varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Name der Gruppe',
	SCHWIERIGKEITSGRAD varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'mapped die enum Schwierigkeitsgrad',
	REFERENZTYP varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Typ der semantischen Referenz entsprechend enum Referenztyp',
	REFERENZ varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID der Referenz, z.B. mk_wettbewerbe.WETTBEWERBE oder matheag.serien',
	STATUS varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'ERFASST' NOT NULL COMMENT 'Status der Gruppe entsprechend Enum DomainEntityStatus',
	KOMMENTAR varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  	GEAENDERT_AM timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	GEAENDERT_DURCH varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID eines ADMINS',
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_raetselgruppen_1 (REFERENZTYP, REFERENZ, SCHWIERIGKEITSGRAD),
	UNIQUE KEY uk_raetselgruppeen_2 (NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

create table mathe_jung_alt.RAETSELGRUPPENELEMENTE(
	UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL PRIMARY KEY,
	NUMMER varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Dient gleichzeitig als Überschrift des Elements in der Sammlung. Die Nummern müssen sortierbar sein, um eine feste Reihenfolge sicherzustellen',
	PUNKTE int(10) COMMENT 'mit 100 multiplizierte Anzahl Punkte bei korrekter Lösung. Anzahl Strafpunkte ist dann PUNKTE / (k-1), wobei k die Anzahl der Antwortvorschläge ist',
	RAETSEL varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID des Rätsels',
	GRUPPE varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID der Rästelgruppe',
	VERSION int(10) DEFAULT 0,
	UNIQUE KEY uk_raetselgruppenelemente_1 (RAETSEL, GRUPPE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE OR REPLACE VIEW mathe_jung_alt.VW_AUFGABEN
AS
select
	r.UUID,
	r.SCHLUESSEL,
	r.ANTWORTVORSCHLAEGE,
	r.NAME,
	e.NUMMER,
	e.PUNKTE,
	vq.ART as QUELLE_ART,
	vq.MEDIUM_TITEL,
	vq.SEITE,
	vq.AUSGABE,
	vq.JAHRGANG,
	vq.PERSON,
	vq.HW,
	vq.DESKRIPTOREN
from
	RAETSEL r,
	RAETSELGRUPPENELEMENTE e,
	VW_QUELLEN vq
where
    e.RAETSEL = r.UUID
	AND r.QUELLE = vq.UUID;


