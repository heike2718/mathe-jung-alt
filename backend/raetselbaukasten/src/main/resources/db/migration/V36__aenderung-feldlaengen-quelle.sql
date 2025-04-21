use mathe_jung_alt;

ALTER TABLE mathe_jung_alt.QUELLEN modify PFAD varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT '(relativer) Pfad zu einer Datei, in der die Aufgabe steht, z.B. /mathe/... zeigt auf /media/veracrypt2/mathe/...';

ALTER TABLE mathe_jung_alt.QUELLEN modify KLASSE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT '(relativer) Pfad zu einer Datei, in der die Aufgabe steht, z.B. /mathe/... zeigt auf /media/veracrypt2/mathe/...';

ALTER TABLE mathe_jung_alt.QUELLEN modify SEITE varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT '(relativer) Pfad zu einer Datei, in der die Aufgabe steht, z.B. /mathe/... zeigt auf /media/veracrypt2/mathe/...';

