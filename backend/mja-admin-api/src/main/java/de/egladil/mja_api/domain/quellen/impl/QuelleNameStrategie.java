// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen.impl;

import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

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

		QuelleNameStrategie result = null;

		switch (quellenart) {

		case PERSON -> result = new PersonquelleNameStrategie();
		case BUCH -> result = new BuchquelleNameStrategie();
		case ZEITSCHRIFT -> result = new ZeitschriftquelleNameStrategie();
		default -> throw new IllegalArgumentException("Unexpected value: " + quellenart);
		}

		return result;
	}

}
