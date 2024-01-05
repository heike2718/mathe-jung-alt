// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen.impl;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.quellen.IQuellenangabeDaten;
import de.egladil.mja_api.domain.quellen.QuelleNameStrategie;
import de.egladil.mja_api.domain.quellen.Quellenart;

/**
 * InternetquelleNameStrategie
 */
public class InternetquelleNameStrategie implements QuelleNameStrategie {
	@Override
	public String getText(final IQuellenangabeDaten quelle) {

		if (quelle.getQuellenart() != Quellenart.INTERNET) {

			throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.INTERNET);
		}

		if (StringUtils.isBlank(quelle.getMediumTitel())) {

			throw new MjaRuntimeException("Bei Quellenart INTERNET darf mediumTitel nicht blank sein.");
		}

		StringBuilder sb = new StringBuilder(quelle.getMediumTitel());

		if (StringUtils.isNotBlank(quelle.getJahr())) {

			sb.append(" (");
			sb.append(quelle.getJahr());
			sb.append(")");
		}

		if (StringUtils.isNotBlank(quelle.getKlasse())) {

			sb.append(", ");
			sb.append(quelle.getKlasse());
		}

		if (StringUtils.isNotBlank(quelle.getStufe())) {

			sb.append(", ");
			sb.append(quelle.getStufe());
		}

		return sb.toString();
	}

}
