// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * QuellenNameTestUtils
 */
public class QuellenNameTestUtils {

	static PersistenteQuelleReadonly createQuelleAlleAttributeOhneQuellenart() {

		PersistenteQuelleReadonly quelle = new PersistenteQuelleReadonly();
		quelle.ausgabe = "11";
		quelle.autor = "Johannes Lehmann";
		quelle.jahr = "1987";
		quelle.klasse = "Klasse 4";
		quelle.mediumTitel = "Grunschulolympiade 2x2";
		quelle.mediumUuid = "m-uuid-1";
		quelle.person = "Hans Manz";
		quelle.seite = "42";
		quelle.sortNumber = 1;
		quelle.stufe = "Stufe 2";
		quelle.userId = "p-uuid-1";
		quelle.uuid = "q-uuid-1";

		return quelle;

	}

}
