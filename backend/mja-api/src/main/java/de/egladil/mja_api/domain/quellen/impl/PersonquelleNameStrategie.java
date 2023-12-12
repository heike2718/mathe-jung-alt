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
 * PersonquelleNameStrategie
 */
public class PersonquelleNameStrategie implements QuelleNameStrategie {

	@Override
	public String getName(final PersistenteQuelleReadonly quelle) {

		if (quelle.quellenart != Quellenart.PERSON) {

			throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.PERSON);
		}

		if (StringUtils.isBlank(quelle.person)) {

			throw new MjaRuntimeException("Bei Quellenart PERSON darf person nicht blank sein.");
		}

		return quelle.person;
	}

}
