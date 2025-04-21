ALTER TABLE RAETSEL MODIFY COLUMN QUELLE varchar(36) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Referenz auf QUELLEN';

ALTER TABLE RAETSEL MODIFY COLUMN ANZAHL_ANTWORTEN tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Anzahl der Antwortvorschläge zur Berechnung von Strafpunkten';

ALTER TABLE RAETSEL MODIFY COLUMN LOESUNG text COLLATE utf8_unicode_ci COMMENT 'LaTeX- Text der ausführlichen Lösung';
