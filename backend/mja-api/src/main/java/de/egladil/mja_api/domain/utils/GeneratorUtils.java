// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;

/**
 * GeneratorUtils
 */
public class GeneratorUtils {

	/**
	 * Je nach Konstellaion sollen Antwortvorschlaege mit gedruckt werden.
	 *
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @param  aufgabe
	 *                                  Quizaufgabe
	 * @return                          boolean
	 */
	public static boolean shouldPrintAntwortvorschlaege(final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final Quizaufgabe aufgabe) {

		return layoutAntwortvorschlaege != LayoutAntwortvorschlaege.NOOP
			&& aufgabe.getAntwortvorschlaege() != null
			&& aufgabe.getAntwortvorschlaege().length > 0;
	}

	/**
	 * Je nach Konstellaion sollen Antwortvorschlaege mit gedruckt werden.
	 *
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @param  input
	 *                                  RaetselGeneratorinput
	 * @return                          boolean
	 */
	public static boolean shouldPrintAntwortvorschlaege(final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final RaetselGeneratorinput input) {

		return layoutAntwortvorschlaege != LayoutAntwortvorschlaege.NOOP
			&& input.getAntwortvorschlaege() != null
			&& input.getAntwortvorschlaege().length > 0;
	}

	/**
	 * Nach dem letzten Element keinen Trenner mehr.
	 *
	 * @param  anzahlAufgaben
	 * @param  count
	 * @return                boolean
	 */
	public static boolean shouldPrintTrenner(final int anzahlAufgaben, final int count) {

		return anzahlAufgaben > 1 && count < anzahlAufgaben - 1;
	}

}
