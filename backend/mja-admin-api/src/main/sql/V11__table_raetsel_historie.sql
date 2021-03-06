create table mathe_jung_alt.HISTORIE_RAETSEL(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	RAETSEL_UUID varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Referenz auf das RAETSEL',
	FRAGE text COLLATE utf8_unicode_ci NOT NULL COMMENT 'LaTeX- Text der Rätselfrage',
  	LOESUNG text COLLATE utf8_unicode_ci NOT NULL COMMENT 'LaTeX- Text der ausführlichen Lösung',
  	GEAENDERT_AM timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  	GEAENDERT_DURCH varchar(36) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'UUID eines ADMINS',
  	VERSION int(10) DEFAULT 0,
	PRIMARY KEY (ID),
	CONSTRAINT fk_historie_raetsel_raetsel FOREIGN KEY (RAETSEL_UUID) REFERENCES RAETSEL (UUID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
