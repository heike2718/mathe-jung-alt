// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen.impl;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * InternetquelleNameStrategie
 */
public class InternetquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getName(final PersistenteQuelleReadonly quelle) {

		if (quelle.quellenart != Quellenart.INTERNET) {

			throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.INTERNET);
		}

		if (StringUtils.isBlank(quelle.mediumTitel)) {

			throw new MjaRuntimeException("Bei Quellenart INTERNET darf mediumTitel nicht blank sein.");
		}

		StringBuilder sb = new StringBuilder(quelle.mediumTitel);

		if (StringUtils.isNotBlank(quelle.jahr)) {

			sb.append(" (");
			sb.append(quelle.jahr);
			sb.append(")");
		}

		if (StringUtils.isNotBlank(quelle.klasse)) {

			sb.append(", ");
			sb.append(quelle.klasse);
		}

		if (StringUtils.isNotBlank(quelle.stufe)) {

			sb.append(", ");
			sb.append(quelle.stufe);
		}

		return sb.toString();
	}

}
