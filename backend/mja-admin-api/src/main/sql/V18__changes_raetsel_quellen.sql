ALTER TABLE RAETSEL ADD COLUMN ADAPTIERT tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Flag, ob dieses Rätsel von der Quelle adaptiert wurde';

ALTER TABLE QUELLEN ADD COLUMN HW tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Flag, ob dies die Quelle für adaptierte Rätsel ist';

UPDATE QUELLEN SET HW = 1;

CREATE OR REPLACE VIEW mathe_jung_alt.VW_QUELLEN
AS
SELECT q.UUID, q.SORTNR, q.ART, m.UUID as MEDIUM_UUID,  m.TITEL AS MEDIUM_TITEL, q.AUSGABE, q.JAHRGANG, q.SEITE, q.PERSON, q.DESKRIPTOREN, q.HW
FROM mathe_jung_alt.QUELLEN q LEFT OUTER JOIN MEDIEN m ON q.MEDIUM = m.UUID;

