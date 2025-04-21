// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen.impl;

import org.apache.commons.lang3.StringUtils;

import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.quellen.IQuellenangabeDaten;
import de.egladil.raetselbaukasten.domain.quellen.QuelleNameStrategie;
import de.egladil.raetselbaukasten.domain.quellen.Quellenart;

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
