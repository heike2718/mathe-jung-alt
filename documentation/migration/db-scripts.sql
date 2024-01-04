-- matheag
select a.id, a.SCHLUESSEL, a.TITEL, a.STUFE, a.ZWECK, a.MIGRATIONSSTATUS, q.id, q.ART as quelleart, a.ART as aufgabenart  from aufgaben a, quellen q  where a.QUELLE_ID = q.ID and a.SCHLUESSEL = '02615';



select schluessel, titel, stufe from aufgaben where art = 'E' order by schluessel desc;

 select * from aufgaben where TITEL  like 'Klasse%Logical mit Kindern%';
-- select SCHLUESSEL, TITEL, STUFE, MIGRATIONSSTATUS from aufgaben where MIGRATIONSSTATUS  is not null order by SCHLUESSEL ;

update aufgaben set MIGRATIONSSTATUS = 'PROD' where schluessel = '02551';

update aufgaben set MIGRATIONSSTATUS = 'DUBLETTE' where SCHLUESSEL = '02787'
-- commit;
/* PROD, DUBLETTE */

select SCHLUESSEL, TITEL, STUFE, MIGRATIONSSTATUS from aufgaben where MIGRATIONSSTATUS  is not null;

select * from aufgaben order by SCHLUESSEL DESC;


select * from aufgaben a  where a.ART = 'E' order by a.SCHLUESSEL;

select * from quellen q, medien m  where q.BUCH_ID = m.ID and q.id = 869;

select * from vw_aufgaben_medien vam where vam.SCHLUESSEL = '02682';

select * from vw_aufgaben_serien vas where vas.SCHLUESSEL = '02614';


update aufgaben a set a.MIGRATIONSSTATUS = 'PROD' where a.SCHLUESSEL = '02614';


select a.SCHLUESSEL,
	a.TITEL,
	a.ART,
	a.MIGRATIONSSTATUS ,
	vm.jahr,
	vm.nummer,
	vam.MEDIUM_TITEL,
	vam.AUSGABE, 
	vam.JAHRGANG, 
	vam.SEITE,
	vam.DATEINAME,
	vam.url
from vw_minikaenguru vm,
	minikaenguru m,
	aufgaben a,
	vw_aufgaben_medien vam 
where vm.jahr  = m.JAHR  
  and vm.schluessel = a.SCHLUESSEL
  and vam.SCHLUESSEL  = a.SCHLUESSEL
order by a.SCHLUESSEL;







