// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.quellen.impl;

import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * BuchquelleNameStrategie
 */
public class BuchquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getName(final PersistenteQuelleReadonly quelle) {

		return quelle.getMediumTitel() + ", S." + quelle.getSeite();
	}

}
