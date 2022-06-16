DELETE FROM DESKRIPTOREN WHERE NAME = 'Arithmetik';

ALTER TABLE DESKRIPTOREN ADD COLUMN KONTEXT varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE DESKRIPTOREN ADD COLUMN KATEGORIE varchar(30) COMMENT 'THEMA, GRUPPE, STUFE, HERKUNFT';

UPDATE DESKRIPTOREN SET KONTEXT = 'MEDIEN,QUELLEN' WHERE NAME IN ('Buch','Zeitschrift');
UPDATE DESKRIPTOREN SET KONTEXT = 'QUELLEN' WHERE NAME IN ('Person');

UPDATE DESKRIPTOREN SET KONTEXT = 'RAETSEL' WHERE NAME NOT IN ('Buch','Person','Zeitschrift');
UPDATE DESKRIPTOREN SET NAME = 'Multiple Choice' WHERE NAME = 'multiple choice';

UPDATE DESKRIPTOREN SET ADMIN = 1;

UPDATE DESKRIPTOREN SET ADMIN = 0 WHERE NAME IN
('Mathematik',
'Inklusion',
'Klassen 1/2',
'Klassen 3/4',
'Klassen 5/6',
'Klassen 7/8',
'ab Klasse 9',
'Grundschule',
'Sekundarstufe 1',
'Sekundarstufe 2',
'Logik',
'Geometrie',
'Kombinatorik',
'Zahlentheorie',
'Wahrscheinlichkeitsrechnung',
'Algebra',
'Analysis',
'Frühling',
'Sommer',
'Herbst',
'Winter',
'Jahreswechsel',
'Multiple Choice'
);

UPDATE DESKRIPTOREN SET KATEGORIE = 'THEMA';

UPDATE DESKRIPTOREN SET KATEGORIE = 'HERKUNFT' WHERE NAME IN
('Buch','Person','Zeitschrift','Zitat','Nachbau');

UPDATE DESKRIPTOREN SET KATEGORIE = 'GRUPPE' WHERE NAME IN
('Minikänguru','Serie');

UPDATE DESKRIPTOREN SET KATEGORIE = 'ART' WHERE NAME IN
('Multiple Choice');

UPDATE DESKRIPTOREN SET KATEGORIE = 'STUFE' WHERE NAME IN
('Inklusion', 'Klasse 1', 'Klasse 2', 'IKID', 'EINS', 'ZWEI',
'A', 'B', 'C', 'Vorschule', 'Klassen 1/2', 'Klassen 3/4',
'Klassen 5/6', 'Klassen 7/8', 'ab Klasse 9');

INSERT INTO DESKRIPTOREN (NAME, KONTEXT, KATEGORIE, ADMIN) VALUES ('Grundschule', 'RAETSEL', 'STUFE', 0);
INSERT INTO DESKRIPTOREN (NAME, KONTEXT, KATEGORIE, ADMIN) VALUES ('Sekundarstufe 1', 'RAETSEL', 'STUFE', 0);
INSERT INTO DESKRIPTOREN (NAME, KONTEXT, KATEGORIE, ADMIN) VALUES ('Sekundarstufe 2', 'RAETSEL', 'STUFE', 0);
INSERT INTO DESKRIPTOREN (NAME, KONTEXT, KATEGORIE, ADMIN) VALUES ('Heft', 'RAETSEL', 'GRUPPE', 1);


