// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.quellen.impl;

import de.egladil.mathe_jung_alt_ws.domain.quellen.Quellenart;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * QuelleNameStrategie
 */
public interface QuelleNameStrategie {

	/**
	 * Bastelt einen Namen für ein Quellenverzeichnis zusammen.
	 *
	 * @param  quelle
	 * @return        String
	 */
	String getName(PersistenteQuelleReadonly quelle);

	static QuelleNameStrategie getStrategie(final Quellenart quellenart) {

		switch (quellenart) {

		case PERSON:
			return new PersonquelleNameStrategie();

		case BUCH:
			return new BuchquelleNameStrategie();

		case ZEITSCHRIFT:
			return new ZeitschriftquelleNameStrategie();

		default:
			throw new IllegalArgumentException("unbekannte Quellenart " + quellenart);
		}
	}

}
