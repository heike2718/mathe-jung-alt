insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (1, 'Mathematik',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (2, 'Minikänguru',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (3,'Inklusion',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (4,'Klasse 1',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (5,'Klasse 2',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (6, 'IKID',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (7, 'EINS',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (8, 'ZWEI',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (9, 'A',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (10, 'B',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (11, 'C',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (12, 'Serie',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (13, 'Zitat',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (14, 'Logik',0,'RAETSEL');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (15, 'Buch',0,'MEDIEN,QUELLEN');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (16, 'Zeitschrift',0,'MEDIEN,QUELLEN');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (17, 'Person',0,'QUELLEN');
insert into DESKRIPTOREN (ID,NAME,ADMIN,KONTEXT) VALUE (18, 'Nachbau',1,'RAETSEL');

INSERT INTO VW_QUELLEN (UUID, SORTNR, ART, MEDIUM_UUID, MEDIUM_TITEL, SEITE, DESKRIPTOREN) VALUES ('8ef4d9b8', 1, 'BUCH', 'cd69615e', 'Johannes Lehmann: 2x3 Plus Spaß dabei', '43', '1,4,5,6,7,8,15');
