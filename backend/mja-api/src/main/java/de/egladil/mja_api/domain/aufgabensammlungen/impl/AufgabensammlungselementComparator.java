// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen.impl;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.egladil.mja_api.domain.aufgabensammlungen.Aufgabensammlungselement;

/**
 * AufgabensammlungselementComparator
 */
public class AufgabensammlungselementComparator implements Comparator<Aufgabensammlungselement> {

	private final Collator collator = Collator.getInstance(Locale.GERMANY);

	@Override
	public int compare(final Aufgabensammlungselement o1, final Aufgabensammlungselement o2) {

		return collator.compare(o1.getNummer(), o2.getNummer());
	}
}
