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
 * BuchquelleNameStrategie
 */
public class BuchquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getText(final IQuellenangabeDaten quelle) {

		if (quelle.getQuellenart() != Quellenart.BUCH) {

			throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.BUCH);
		}

		if (StringUtils.isBlank(quelle.getMediumTitel())) {

			throw new MjaRuntimeException("Bei Quellenart BUCH darf mediumTitel nicht blank sein.");
		}

		if (StringUtils.isBlank(quelle.getAutor())) {

			throw new MjaRuntimeException("Bei Quellenart BUCH darf autor nicht blank sein.");
		}

		if (StringUtils.isBlank(quelle.getSeite())) {

			throw new MjaRuntimeException("Bei Quellenart BUCH darf seite nicht blank sein.");
		}

		return quelle.getAutor() + ": " + quelle.getMediumTitel() + ", S." + quelle.getSeite();
	}

}
