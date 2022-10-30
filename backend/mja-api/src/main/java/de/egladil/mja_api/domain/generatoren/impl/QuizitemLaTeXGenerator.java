// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import javax.enterprise.context.ApplicationScoped;

import de.egladil.mja_api.domain.utils.MjaFileUtils;

/**
 * QuizitemLaTeXGenerator
 */
@ApplicationScoped
public class QuizitemLaTeXGenerator {

	private static final String LATEX_QUIZITEM_FRAGE_LOESUNG = "/latex/template-raetsel-png.tex";

	public String generateLaTeX() {

		String template = MjaFileUtils.loadTemplate(LATEX_QUIZITEM_FRAGE_LOESUNG);

		/*
		 * {content-frage}
		 * {antwortvorschlaege}
		 * {par}
		 * {loesungsbuchstabe}
		 * {content-loesung}
		 */

		return null;
	}

}
