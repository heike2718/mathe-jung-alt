// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.utils.MjaFileUtils;

/**
 * QuizitemLaTeXGenerator
 */
public class QuizitemLaTeXGenerator {

	private static final String LATEX_QUIZITEM_FRAGE_LOESUNG = "/latex/template-quiz-frage-loesung.tex";

	/**
	 * Generiert ein Stück LaTeX, das in ein Dokument eingefügt werden kann.
	 *
	 * @param  input
	 *               RaetselGeneratorinput die Texte des Rätsels, die gedruckt werden sollen.
	 * @return       String<br>
	 *               <br>
	 *               TODO: hier noch Flexibilität rein mit Lösunh auf nächste Seite oder so,
	 */
	public String generateLaTeX(final RaetselGeneratorinput input) {

		String template = MjaFileUtils.loadTemplate(LATEX_QUIZITEM_FRAGE_LOESUNG);

		template = template.replace(LaTeXPlaceholder.CONTENT_FRAGE.placeholder(), input.getFrage());

		String antworten = AntwortvorschlagGeneratorStrategegy.create(input.getLayoutAntwortvorschlaege())
			.generateLaTeXAntwortvorschlaege(input.getAntwortvorschlaege());

		template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), antworten);

		String loesungsbuchstabe = getTextLoesungsbuchstabe(input.getAntwortvorschlaege());

		if (StringUtils.isNotBlank(loesungsbuchstabe)) {

			template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), loesungsbuchstabe);
		}

		if (StringUtils.isNotBlank(input.getLoesung())) {

			template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), input.getLoesung());
		}

		return template;
	}

	String getTextLoesungsbuchstabe(final Antwortvorschlag[] antwortvorschlaege) {

		return new LoesungsbuchstabeTextGenerator().getTextLoesungsbuchstabe(antwortvorschlaege);

	}

}
