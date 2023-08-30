// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.raetsel.RaetselService;

/**
 * RaetselgruppeLaTeXGeneratorStrategy. Erzeugt das LaTeX für eine Raetselgruppe.
 */
public interface RaetselgruppeLaTeXGeneratorStrategy {

	/**
	 * Generiert das LaTeX für die gegebene Raetselgruppe.
	 *
	 * @param  input
	 *                                RaetselgruppeGeneratorInput - die Parameter für das Generieren.
	 * @param  templatesService
	 *                                LaTeXTemplatesService - der Service, der die passenden Templates zur Verfügung stellt.
	 * @param  raetselService
	 *                                RaetselService
	 * @param  quizitemLaTeXGenerator
	 *                                QuizitemLaTeXGenerator
	 * @return
	 */
	String generateLaTeX(RaetselgruppeGeneratorInput input, LaTeXTemplatesService templatesService, RaetselService raetselService, QuizitemLaTeXGenerator quizitemLaTeXGenerator);

	static RaetselgruppeLaTeXGeneratorStrategy getStrategy(final Verwendungszweck verwendungszweck) {

		switch (verwendungszweck) {

		case ARBEITSBLATT:
			return new ArbeitsblattLaTeXGeneratorStrategy();

		case AUFGABEN_MIT_LOESUNGEN:
		case VORSCHAU:
		case LATEX:
			return new AufgabenLoesungenLaTeXGeneratorStrategy();

		case KARTEI:
			return new KarteiLaTeXGeneratorStrategy();

		default:
			throw new IllegalArgumentException("Unexpected value: " + verwendungszweck);
		}

	}

}
