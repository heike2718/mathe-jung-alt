use mathe_jung_alt;

update DESKRIPTOREN SET ADMIN = 0 where NAME = 'Vorschule';

insert into DESKRIPTOREN (NAME, ADMIN, VERSION) values ('Allgemeinwissen', 0, 0);
insert into DESKRIPTOREN (NAME, ADMIN, VERSION) values ('Biologie', 0, 0);
insert into DESKRIPTOREN (NAME, ADMIN, VERSION) values ('Physik', 0, 0);
