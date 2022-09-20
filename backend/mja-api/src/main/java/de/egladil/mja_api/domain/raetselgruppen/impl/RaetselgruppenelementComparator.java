// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.impl;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.egladil.mja_api.domain.raetselgruppen.Raetselgruppenelement;

/**
 * RaetselgruppenelementComparator
 */
public class RaetselgruppenelementComparator implements Comparator<Raetselgruppenelement> {

	private final Collator collator = Collator.getInstance(Locale.GERMANY);

	@Override
	public int compare(final Raetselgruppenelement o1, final Raetselgruppenelement o2) {

		return collator.compare(o1.getNummer(), o2.getNummer());
	}
}
