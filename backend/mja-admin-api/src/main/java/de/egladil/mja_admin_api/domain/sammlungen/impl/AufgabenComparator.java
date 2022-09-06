// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.impl;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.egladil.mja_admin_api.domain.sammlungen.dto.Aufgabe;

/**
 * AufgabenComparator
 */
public class AufgabenComparator implements Comparator<Aufgabe> {

	private final Collator collator = Collator.getInstance(Locale.GERMANY);

	@Override
	public int compare(final Aufgabe o1, final Aufgabe o2) {

		return collator.compare(o1.getNummer(), o2.getNummer());
	}

}
