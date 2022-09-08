create table mathe_jung_alt.DESKRIPTOREN(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	NAME varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci  NOT NULL COMMENT 'eindeutiger Name eines Deskriptors',
	ADMIN  tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Flag, ob dieser Deskriptor nur durch den ADMIN vergeben und verwendet werden kann',
	VERSION int(10) DEFAULT 0,
	PRIMARY KEY (ID),
  	UNIQUE KEY uk_deskriptoren_1 (NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Mathematik');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Minikänguru');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Inklusion');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Klasse 1');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Klasse 2');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('IKID');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('EINS');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('ZWEI');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('A');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('B');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('C');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Vorschule');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Klassen 1/2');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Klassen 3/4');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Klassen 5/6');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Klassen 7/8');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('ab Klasse 9');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Serie');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Zitat');
insert into mathe_jung_alt.DESKRIPTOREN (NAME, ADMIN) VALUE ('Nachbau', 1);
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Logik');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Algebra');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Arithmetik');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Geometrie');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Analysis');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Kombinatorik');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Zahlentheorie');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Wahrscheinlichkeit');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('multiple choice');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Frühling');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Sommer');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Herbst');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Winter');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Weihnachten');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Jahreswechsel');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Ostern');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Jahreszeit egal');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Buch');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Zeitschrift');
insert into mathe_jung_alt.DESKRIPTOREN (NAME) VALUE ('Person');

