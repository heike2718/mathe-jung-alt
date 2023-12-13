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
 * ZeitschriftquelleNameStrategie
 */
public class ZeitschriftquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getText(final PersistenteQuelleReadonly quelle) {

		if (quelle.quellenart != Quellenart.ZEITSCHRIFT) {

			throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.ZEITSCHRIFT);
		}

		if (StringUtils.isBlank(quelle.mediumTitel)) {

			throw new MjaRuntimeException("Bei Quellenart ZEITSCHRIFT darf mediumTitel nicht blank sein.");
		}

		StringBuffer sb = new StringBuffer(quelle.mediumTitel);

		if (StringUtils.isNotBlank(quelle.ausgabe)) {

			sb.append(" (");
			sb.append(quelle.ausgabe);
			sb.append(")");
		}

		if (StringUtils.isNotBlank(quelle.jahr)) {

			sb.append(" ");
			sb.append(quelle.jahr);
		}

		if (StringUtils.isNotBlank(quelle.seite)) {

			sb.append(", S.");
			sb.append(quelle.seite);
		}

		return sb.toString();
	}

}
