// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.raetsel.RaetselService;

/**
 * RaetselgruppeGeneratorStrategy. Erzeugt das LaTeX für eine Raetselgruppe.
 */
public interface RaetselgruppeGeneratorStrategy {

	/**
	 * Generiert das LaTeX für die gegebene Raetselgruppe.
	 *
	 * @param  input
	 *                                RaetselgruppeGeneratorInput - die Parameter für das Generieren.
	 * @param  raetselDao
	 *                                RaetselService
	 * @param  quizitemLaTeXGenerator
	 *                                QuizitemLaTeXGenerator
	 * @return
	 */
	String generateLaTeX(RaetselgruppeGeneratorInput input, RaetselService raetselService, QuizitemLaTeXGenerator quizitemLaTeXGenerator);

	static RaetselgruppeGeneratorStrategy getStrategy(final Verwendungszweck verwendungszweck) {

		switch (verwendungszweck) {

		case ARBEITSBLATT:
		case VORSCHAU:
			return new AufgabenLoesungenLaTeXGeneratorStrategy();

		case KARTEI:
			return new KarteiLaTeXGeneratorStrategy();

		case LATEX:
			throw new MjaRuntimeException("LaTeX wird als zip-Archiv generiert und verwendet andere Generatoren.");

		default:
			throw new IllegalArgumentException("Unexpected value: " + verwendungszweck);
		}

	}

}
