use mathe_jung_alt;

alter table mathe_jung_alt.RAETSEL add column `ANTWORTVORSCHLAEGE_EINGEBETTET` int(1) default 0 not null COMMENT 'Flag, das anzeigt, ob der Text eventuell vorhandene Antwortvorschläge bereits enthält.';


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
	r.ANTWORTVORSCHLAEGE_EINGEBETTET,
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

