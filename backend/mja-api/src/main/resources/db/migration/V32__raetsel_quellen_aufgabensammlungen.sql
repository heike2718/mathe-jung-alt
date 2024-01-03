use mathe_jung_alt;

delete from mathe_jung_alt.DESKRIPTOREN where KONTEXT != 'RAETSEL';

drop VIEW mathe_jung_alt.VW_AUFGABEN;

alter table mathe_jung_alt.RAETSEL add column `HERKUNFT` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Typ der Herkunft des Rätsels: EIGENKREATION | ZITAT | ADAPTATION';
update mathe_jung_alt.RAETSEL set HERKUNFT = 'EIGENKREATION';
alter table mathe_jung_alt.RAETSEL modify column `HERKUNFT` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci not null COMMENT 'Typ der Herkunft des Rätsels: EIGENKREATION | ZITAT | ADAPTATION';

alter table mathe_jung_alt.RAETSEL add column `FREIGEGEBEN` int(1) default 0 not null COMMENT 'Flag, das anzeigt, ob das Rätsel freigegeben ist';

update mathe_jung_alt.RAETSEL set FREIGEGEBEN = 0 where STATUS = 'ERFASST';
update mathe_jung_alt.RAETSEL set FREIGEGEBEN = 1 where STATUS = 'FREIGEGEBEN';


alter table mathe_jung_alt.RAETSEL drop column STATUS;


alter table mathe_jung_alt.RAETSELGRUPPEN RENAME TO mathe_jung_alt.AUFGABENSAMMLUNGEN;
alter table mathe_jung_alt.AUFGABENSAMMLUNGEN add column `PRIVAT` int(1) default 0 not null COMMENT 'Flag, das anzeigt, ob die Aufgabensammlung privat (1) oder öffentlich ist';
alter table mathe_jung_alt.AUFGABENSAMMLUNGEN add column `FREIGEGEBEN` int(1) default 0 not null COMMENT 'Flag, das anzeigt, ob die Aufgabensammlung freigegeben ist';

update mathe_jung_alt.AUFGABENSAMMLUNGEN set FREIGEGEBEN = 0 where STATUS = 'ERFASST';
update mathe_jung_alt.AUFGABENSAMMLUNGEN set FREIGEGEBEN = 1 where STATUS = 'FREIGEGEBEN';

alter table mathe_jung_alt.AUFGABENSAMMLUNGEN drop column STATUS;

alter table mathe_jung_alt.RAETSELGRUPPENELEMENTE RENAME TO mathe_jung_alt.AUFGABENSAMMLUNGSELEMENTE;
ALTER TABLE mathe_jung_alt.AUFGABENSAMMLUNGSELEMENTE CHANGE COLUMN GRUPPE SAMMLUNG varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID der AUFGABENSAMMLUNG';
alter table mathe_jung_alt.AUFGABENSAMMLUNGSELEMENTE add column `SEITENUMBRUCH` int(1) default 0 not null COMMENT 'Flag, das anzeigt, ob beim Drucken als Arbeitsblatt _NACH_ dieser Aufgabe ein Seitenmbruch gedruckt werden soll (1)';
alter table mathe_jung_alt.AUFGABENSAMMLUNGSELEMENTE add column `MARGIN_BOTTOM` int(2) default 5 not null COMMENT 'Abstand zur Nachfolgeraufgabe beim Drucken als Arbeitsblatt in mm';
alter table mathe_jung_alt.AUFGABENSAMMLUNGSELEMENTE add column `SORTNR` int(3) default 0 not null COMMENT 'legt die Reihenfolge in der Aufgabensammlung fest, beginnend mit Index 0';

ALTER TABLE mathe_jung_alt.AUFGABENSAMMLUNGSELEMENTE
ADD CONSTRAINT fk_aufgabensammlung
FOREIGN KEY (SAMMLUNG) REFERENCES mathe_jung_alt.AUFGABENSAMMLUNGEN (UUID)
ON DELETE CASCADE
ON UPDATE RESTRICT;

ALTER TABLE mathe_jung_alt.MEDIEN drop column DESKRIPTOREN;
ALTER TABLE mathe_jung_alt.MEDIEN add column AUTOR varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Autor eines Buches';
ALTER TABLE mathe_jung_alt.MEDIEN add column URL varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'URL, falls bekannt bei Art INTERNET';
ALTER TABLE mathe_jung_alt.MEDIEN modify column PFAD varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'relativer Pfad auf device, z.B. /mathe/... zeigt auf /media/veracrypt2/mathe/...';

ALTER TABLE mathe_jung_alt.QUELLEN add column KLASSE varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Klassenstufe bei Wettbewerben, z.B. Klasse 2';
ALTER TABLE mathe_jung_alt.QUELLEN add column STUFE varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'Stufe bei Wettbewerben, z.B. Stufe 3';
ALTER TABLE mathe_jung_alt.QUELLEN change JAHRGANG JAHR varchar(4) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT 'bei Zeitschriften das Erscheinungsjahr, bei Wettbewerben das Wettbewerbsjahr';

CREATE OR REPLACE VIEW mathe_jung_alt.VW_QUELLEN
AS
SELECT q.UUID,
	q.SORTNR,
	q.ART,
	m.UUID as MEDIUM_UUID,
	m.TITEL AS MEDIUM_TITEL,
	m.AUTOR,
	q.AUSGABE,
	q.JAHR,
	q.KLASSE,
	q.STUFE,
	q.SEITE,
	q.PERSON,
	q.USER_ID
FROM mathe_jung_alt.QUELLEN q
	LEFT OUTER JOIN mathe_jung_alt.MEDIEN m ON q.MEDIUM = m.UUID;


alter table mathe_jung_alt.QUELLEN drop column DESKRIPTOREN;


CREATE OR REPLACE VIEW mathe_jung_alt.VW_AUFGABEN
AS
select
	r.UUID,
	r.NAME,
	r.SCHLUESSEL,
	r.FREIGEGEBEN,
	r.FILENAME_VORSCHAU_FRAGE,
	r.FILENAME_VORSCHAU_LOESUNG,
	r.DESKRIPTOREN,
	r.HERKUNFT,
	e.NUMMER,
	e.PUNKTE,
	e.SAMMLUNG,
	e.SORTNR,
	e.SEITENUMBRUCH,
	e.MARGIN_BOTTOM,
	r.ANTWORTVORSCHLAEGE,
	vq.ART as QUELLE_ART,
	vq.MEDIUM_TITEL,
	vq.AUTOR,
	vq.SEITE,
	vq.AUSGABE,
	vq.JAHR,
	vq.KLASSE,
	vq.STUFE,
	vq.PERSON,
	vq.USER_ID
from
	mathe_jung_alt.RAETSEL r,
	mathe_jung_alt.AUFGABENSAMMLUNGSELEMENTE e,
	mathe_jung_alt.VW_QUELLEN vq
where
    e.RAETSEL = r.UUID
	AND r.QUELLE = vq.UUID;

