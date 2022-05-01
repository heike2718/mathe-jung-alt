// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.quellen.impl;

import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistenteQuelleReadonly;

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
