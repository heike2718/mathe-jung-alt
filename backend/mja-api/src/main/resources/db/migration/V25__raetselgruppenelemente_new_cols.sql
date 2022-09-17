ALTER TABLE mathe_jung_alt.RAETSELGRUPPENELEMENTE
ADD COLUMN PUNKTE int(10) COMMENT 'mit 100 multiplizierte Anzahl Punkte bei korrekter Lösung. Anzahl Strafpunkte ist dann PUNKTE / (k-1), wobei k die Anzahl der Antwortvorschläge ist';

CREATE OR REPLACE VIEW mathe_jung_alt.VW_AUFGABEN
AS
select
	r.UUID,
	r.SCHLUESSEL,
	r.ANTWORTVORSCHLAEGE,
	e.PUNKTE,
	vq.ART,
	vq.MEDIUM_TITEL,
	vq.SEITE,
	vq.AUSGABE,
	vq.JAHRGANG,
	vq.PERSON,
	vq.HW,
	vq.DESKRIPTOREN
from
	RAETSEL r,
	VW_QUELLEN vq,
	RAETSELGRUPPENELEMENTE e
where
	r.QUELLE = vq.UUID
	AND e.RAETSEL = r.UUID;
