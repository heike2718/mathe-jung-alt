ALTER TABLE RAETSEL DROP INDEX ind_raetsel_ft;

ALTER TABLE RAETSEL ADD FULLTEXT INDEX ind_raetsel_ft (SCHLUESSEL, NAME, KOMMENTAR, FRAGE, LOESUNG);
