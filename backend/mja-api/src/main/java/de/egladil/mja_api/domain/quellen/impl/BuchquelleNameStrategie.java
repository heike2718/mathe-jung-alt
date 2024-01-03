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
 * BuchquelleNameStrategie
 */
public class BuchquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getText(final PersistenteQuelleReadonly quelle) {

		if (quelle.quellenart != Quellenart.BUCH) {

			throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.BUCH);
		}

		if (StringUtils.isBlank(quelle.mediumTitel)) {

			throw new MjaRuntimeException("Bei Quellenart BUCH darf mediumTitel nicht blank sein.");
		}

		if (StringUtils.isBlank(quelle.autor)) {

			throw new MjaRuntimeException("Bei Quellenart BUCH darf autor nicht blank sein.");
		}

		if (StringUtils.isBlank(quelle.seite)) {

			throw new MjaRuntimeException("Bei Quellenart BUCH darf seite nicht blank sein.");
		}

		return quelle.autor + ": " + quelle.mediumTitel + ", S." + quelle.seite;
	}

}
