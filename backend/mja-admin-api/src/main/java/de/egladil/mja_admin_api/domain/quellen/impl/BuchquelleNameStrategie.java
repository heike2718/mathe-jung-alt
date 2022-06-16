// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.quellen.impl;

import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * BuchquelleNameStrategie
 */
public class BuchquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getName(final PersistenteQuelleReadonly quelle) {

		return quelle.getMediumTitel() + ", S." + quelle.getSeite();
	}

}
