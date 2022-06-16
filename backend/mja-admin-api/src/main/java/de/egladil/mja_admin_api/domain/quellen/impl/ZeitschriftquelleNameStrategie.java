// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.quellen.impl;

import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * ZeitschriftquelleNameStrategie
 */
public class ZeitschriftquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getName(final PersistenteQuelleReadonly quelle) {

		String nameAusgabeJahr = quelle.getMediumTitel() + " (" + quelle.getAusgabe() + ") " + quelle.getJahrgang();

		return quelle.getSeite() == null ? nameAusgabeJahr : nameAusgabeJahr + ", S." + quelle.getSeite();
	}

}
