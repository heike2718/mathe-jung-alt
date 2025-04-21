-- da mariadb den ersten timestamp in der column automatisch mit den defaults versieht, dies bei TIME_OCCURED aber
-- blöd ist, spendieren wir eine weitere column DATE_MODIFIED

-- !immer CHARACTER SET utf8 COLLATE utf8_unicode_ci, weil COLLATE ...general... die diakritischen Zeichen entfernt und damit nicht korrekt sortiert

CREATE TABLE EVENTS (
	`ID` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	`DATE_MODIFIED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'wird automatisch von mariadb gesetzt',
  	`NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
	`TIME_OCCURED` timestamp NOT NULL,
  	`BODY` JSON,
	`VERSION` int(10) DEFAULT 0,
	CHECK (JSON_VALID(BODY))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'speichert events aus der domain';

