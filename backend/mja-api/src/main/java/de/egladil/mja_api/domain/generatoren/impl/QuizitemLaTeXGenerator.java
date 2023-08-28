// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * QuizitemLaTeXGenerator
 */
@ApplicationScoped
public class QuizitemLaTeXGenerator {

	private static final String LATEX_QUIZITEM_FRAGE_LOESUNG = "/latex/template-quiz-frage-loesung.tex";

	private String templateFrageLoesung;

	/**
	 * Generiert ein Stück LaTeX, das in ein Dokument eingefügt werden kann.
	 *
	 * @param  input
	 *               RaetselGeneratorinput die Texte des Rätsels, die gedruckt werden sollen.
	 * @return       String
	 */
	public String generateLaTeXFrageLoesung(final RaetselGeneratorinput input, final FontName font) {

		String template = new String(this.getTemplate());

		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), font.getLatexFileInputDefinition());

		template = template.replace(LaTeXPlaceholder.CONTENT_FRAGE.placeholder(), input.getFrage());

		String antworten = AntwortvorschlagGeneratorStrategegy.create(input.getLayoutAntwortvorschlaege())
			.generateLaTeXAntwortvorschlaege(input.getAntwortvorschlaege());

		template = template.replace(LaTeXPlaceholder.ANTWORTVORSCHLAEGE.placeholder(), antworten);

		String loesungsbuchstabe = getTextLoesungsbuchstabe(input.getAntwortvorschlaege());

		if (StringUtils.isNotBlank(loesungsbuchstabe)) {

			if (input.isZweiseitig()) {

				template = template.replace(LaTeXPlaceholder.NEWPAGE.placeholder(), LaTeXConstants.VALUE_NEWPAGE);
			} else {

				template = template.replace(LaTeXPlaceholder.NEWPAGE.placeholder(), "");
			}

			template = template.replace(LaTeXPlaceholder.LOESUNGSBUCHSTABE.placeholder(), loesungsbuchstabe);
		}

		if (StringUtils.isNotBlank(input.getLoesung())) {

			if (input.isZweiseitig()) {

				template = template.replace(LaTeXPlaceholder.NEWPAGE.placeholder(), LaTeXConstants.VALUE_NEWPAGE);
			} else {

				template = template.replace(LaTeXPlaceholder.NEWPAGE.placeholder(), "");
			}

			template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), input.getLoesung());
		} else {

			template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), "");
		}

		return template;
	}

	String getTextLoesungsbuchstabe(final Antwortvorschlag[] antwortvorschlaege) {

		return new LoesungsbuchstabeTextGenerator().getTextLoesungsbuchstabe(antwortvorschlaege);

	}

	synchronized String getTemplate() {

		if (templateFrageLoesung == null) {

			templateFrageLoesung = MjaFileUtils.loadTemplate(LATEX_QUIZITEM_FRAGE_LOESUNG);
		}
		return templateFrageLoesung;
	}

}
