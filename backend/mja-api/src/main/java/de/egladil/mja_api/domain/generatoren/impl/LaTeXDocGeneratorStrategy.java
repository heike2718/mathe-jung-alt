// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.List;

import de.egladil.mja_api.domain.generatoren.dto.AufgabensammlungGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;

/**
 * LaTeXDocGeneratorStrategy
 */
public interface LaTeXDocGeneratorStrategy {

	/**
	 * Generiert das LaTeX für ein vollständig expandiertes LaTex, in dem also Fragen und Lösungen nicht importiert werden.
	 *
	 * @param  aufgaben
	 * @param  raetselLaTeX
	 * @param  quizitemLaTeXGenerator
	 * @param  input
	 * @return
	 */
	String generateLaTeX(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final AufgabensammlungGeneratorInput input);

	static LaTeXDocGeneratorStrategy getStrategy(final LaTeXDocGeneratorType generatorType) {

		LaTeXDocGeneratorStrategy result = null;

		switch (generatorType) {

		case MAIN_AUFGABEN -> result = new LaTeXMainDocAufgabenGenerator();
		case MAIN_LOESUNGEN -> result = new LaTeXMainDocLoesungenGenerator();
		case SELFCONTAINED -> result = new LaTeXSelfcontainedGenerator();
		default -> throw new IllegalArgumentException("unbekannter LaTeXDocGeneratorType " + generatorType);

		}

		return result;

	}
}
