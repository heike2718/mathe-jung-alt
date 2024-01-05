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
 * ZeitschriftquelleNameStrategie
 */
public class ZeitschriftquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getText(final IQuellenangabeDaten quelle) {

		if (quelle.getQuellenart() != Quellenart.ZEITSCHRIFT) {

			throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.ZEITSCHRIFT);
		}

		if (StringUtils.isBlank(quelle.getMediumTitel())) {

			throw new MjaRuntimeException("Bei Quellenart ZEITSCHRIFT darf mediumTitel nicht blank sein.");
		}

		StringBuffer sb = new StringBuffer(quelle.getMediumTitel());

		if (StringUtils.isNotBlank(quelle.getAusgabe())) {

			sb.append(" (");
			sb.append(quelle.getAusgabe());
			sb.append(")");
		}

		if (StringUtils.isNotBlank(quelle.getJahr())) {

			sb.append(" ");
			sb.append(quelle.getJahr());
		}

		if (StringUtils.isNotBlank(quelle.getSeite())) {

			sb.append(", S.");
			sb.append(quelle.getSeite());
		}

		return sb.toString();
	}

}
