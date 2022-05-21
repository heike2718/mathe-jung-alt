ALTER TABLE  DESKRIPTOREN DROP COLUMN KATEGORIE;

DELETE FROM DESKRIPTOREN WHERE NAME = 'Jahreszeit egal';

UPDATE DESKRIPTOREN SET ADMIN = 0 WHERE NAME IN ('Klasse 1', 'Klasse 2', 'Wahrscheinlichkeit', 'Weihnachten', 'Ostern');

INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('Quiz', 1, 'RAETSEL');

INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('A-1', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('A-2', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('A-3', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('A-4', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('A-5', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('B-1', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('B-2', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('B-3', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('B-4', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('B-5', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('C-1', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('C-2', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('C-3', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('C-4', 1, 'RAETSEL');
INSERT INTO DESKRIPTOREN (NAME, ADMIN, KONTEXT) VALUES ('C-5', 1, 'RAETSEL');
