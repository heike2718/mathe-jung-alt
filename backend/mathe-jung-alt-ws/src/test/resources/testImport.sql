insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (1, 'Mathematik',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (2, 'Minikänguru',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (3,'Inklusion',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (4,'Klasse 1',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (5,'Klasse 2',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (6, 'IKID',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (7, 'EINS',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (8, 'ZWEI',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (9, 'A',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (10, 'B',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (11, 'C',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (12, 'Serie',1,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (13, 'Zitat',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (14, 'Logik',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (15, 'Buch',1,'MEDIEN,QUELLEN');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (16, 'Zeitschrift',1,'MEDIEN,QUELLEN');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (17, 'Person',1,'QUELLEN');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUES (29, 'Multiple Choice',0,'RAETSEL');

INSERT INTO QUELLEN (UUID, SORTNR, ART, PERSON, DESKRIPTOREN, GEAENDERT_AM, GEAENDERT_DURCH,HW)
VALUES ('8ef4d9b8-62a6-4643-8674-73ebaec52d98',1,'PERSON','Ponder Stibbons','40','2022-06-01 08:38:06','20721575-8c45-4201-a025-7a9fece1f2aa',1);

INSERT INTO VW_QUELLEN (UUID, SORTNR, ART, PERSON, DESKRIPTOREN, HW) VALUES ('8ef4d9b8-62a6-4643-8674-73ebaec52d98', 1, 'PERSON', 'Ponder Stibbons', '40',1);

INSERT INTO RAETSEL (UUID, SCHLUESSEL, NAME, QUELLE, DESKRIPTOREN, KOMMENTAR, ANTWORTVORSCHLAEGE, VERSION, GEAENDERT_DURCH, GEAENDERT_AM, STATUS, ADAPTIERT, FRAGE, LOESUNG)
VALUES ('7a94e100-85e9-4ffb-903b-06835851063b','02789','2022 zählen','8ef4d9b8-62a6-4643-8674-73ebaec52d98','1,2,4,7,9,29','Minikänguru 2022 Klasse 1',
'[{"buchstabe":"A","text":"7 Mal","korrekt":false},{"buchstabe":"B","text":"9 Mal","korrekt":false},{"buchstabe":"C","text":"8 Mal","korrekt":true},{"buchstabe":"D","text":"10 Mal","korrekt":false},{"buchstabe":"E","text":"11 Mal","korrekt":false}]'
,8,'20721575-8c45-4201-a025-7a9fece1f2aa','2022-05-21 07:50:06','ERFASST',0,'\begin{center}{\Large \bf 2 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2 0 0 2 2 2 0 2 2 2 0 2 2 2 0 2 2}\end{center}Wie oft steht hier 2022?',
'Umrande alle 2022, dann kannst du sie leicht zählen.');

INSERT INTO RAETSEL (UUID, SCHLUESSEL, NAME, QUELLE, DESKRIPTOREN, KOMMENTAR, ANTWORTVORSCHLAEGE, VERSION, GEAENDERT_DURCH, GEAENDERT_AM, STATUS, ADAPTIERT, FRAGE, LOESUNG)
VALUES ('cb1f6adb-1ba4-4aeb-ac8d-d4ba255a5866','02622','Bälle in Kisten','8ef4d9b8-62a6-4643-8674-73ebaec52d98','1,4,7,9,29','Serie irgendeine',
'[{"buchstabe":"A","text":"8","korrekt":false},{"buchstabe":"B","text":"7","korrekt":false},{"buchstabe":"C","text":"13","korrekt":false},{"buchstabe":"D","text":"6","korrekt":true},{"buchstabe":"E","text":"3","korrekt":false}]',1,'20721575-8c45-4201-a025-7a9fece1f2aa','2022-05-30 07:50:06','ERFASST',0,'In drei Kisten liegen insgesamt 15 Bälle. In einer Kiste liegen 2 Bälle. In einer Kiste liegen 7 Bälle.','$15 - 2 - 7 = 9$');


INSERT INTO HISTORIE_RAETSEL (ID,RAETSEL_UUID, GEAENDERT_AM, GEAENDERT_DURCH, FRAGE, LOESUNG)
VALUES (2,'7a94e100-85e9-4ffb-903b-06835851063b','2022-05-30 06:32:47','20721575-8c45-4201-a025-7a9fece1f2aa',
'\begin{center}{\Large \bf 2 0 2 2}\end{center}Wie oft steht hier 2022?'
,'2022 umranden, dann zählen');
