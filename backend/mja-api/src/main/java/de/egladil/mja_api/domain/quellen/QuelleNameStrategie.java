// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.quellen.impl.BuchquelleNameStrategie;
import de.egladil.mja_api.domain.quellen.impl.InternetquelleNameStrategie;
import de.egladil.mja_api.domain.quellen.impl.PersonquelleNameStrategie;
import de.egladil.mja_api.domain.quellen.impl.ZeitschriftquelleNameStrategie;

/**
 * QuelleNameStrategie
 */
public interface QuelleNameStrategie {

	/**
	 * Bastelt einen Text für ein Quellenverzeichnis zusammen.
	 *
	 * @param  quelle
	 * @return        String
	 */
	String getText(IQuellenangabeDaten quelle);

	static QuelleNameStrategie getStrategie(final Quellenart quellenart) {

		QuelleNameStrategie result = null;

		switch (quellenart) {

		case PERSON -> result = new PersonquelleNameStrategie();
		case BUCH -> result = new BuchquelleNameStrategie();
		case INTERNET -> result = new InternetquelleNameStrategie();
		case ZEITSCHRIFT -> result = new ZeitschriftquelleNameStrategie();
		default -> throw new IllegalArgumentException("Unexpected value: " + quellenart);
		}

		return result;
	}

	static QuelleNameStrategie getStrategie(final Medienart quellenart) {

		QuelleNameStrategie result = null;

		switch (quellenart) {

		case BUCH -> result = new BuchquelleNameStrategie();
		case INTERNET -> result = new InternetquelleNameStrategie();
		case ZEITSCHRIFT -> result = new ZeitschriftquelleNameStrategie();
		default -> throw new IllegalArgumentException("Unexpected value: " + quellenart);
		}

		return result;
	}

}
