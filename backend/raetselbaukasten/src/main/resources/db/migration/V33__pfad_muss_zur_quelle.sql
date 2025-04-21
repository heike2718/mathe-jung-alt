use mathe_jung_alt;

alter table mathe_jung_alt.MEDIEN drop column `PFAD`;
alter table mathe_jung_alt.DESKRIPTOREN drop column KONTEXT;

ALTER TABLE mathe_jung_alt.QUELLEN add column PFAD varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT '(relativer) Pfad zu einer Datei, in der die Aufgabe steht, z.B. /mathe/... zeigt auf /media/veracrypt2/mathe/...';

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
	q.PFAD,
	q.USER_ID
FROM mathe_jung_alt.QUELLEN q
	LEFT OUTER JOIN mathe_jung_alt.MEDIEN m ON q.MEDIUM = m.UUID;

