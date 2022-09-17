// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quiz.impl;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;

/**
 * QuizaufgabeComparator
 */
public class QuizaufgabeComparator implements Comparator<Quizaufgabe> {

	private final Collator collator = Collator.getInstance(Locale.GERMANY);

	@Override
	public int compare(final Quizaufgabe o1, final Quizaufgabe o2) {

		return collator.compare(o1.getNummer(), o2.getNummer());
	}

}
