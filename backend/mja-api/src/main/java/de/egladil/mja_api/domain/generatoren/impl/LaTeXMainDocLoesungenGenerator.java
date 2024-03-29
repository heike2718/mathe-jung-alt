// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.dto.AufgabensammlungGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;

/**
 * LaTeXMainDocLoesungenGenerator generiert ein LaTeX-File, in dem die tex-Files mit den Lösungen mittels input integriert sind.
 */
public class LaTeXMainDocLoesungenGenerator implements LaTeXDocGeneratorStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(LaTeXMainDocLoesungenGenerator.class);

	/**
	 * Generiert das LaTeX-Masterfile für die Lösungen. Es werden die input-Befehle zum Importieren der Lösungen-Files generiert.
	 *
	 * @param  aufgaben
	 * @param  raetselLaTeX
	 * @param  quizitemLaTeXGenerator
	 * @param  input
	 * @return
	 */
	public String generateLaTeX(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final AufgabensammlungGeneratorInput input) {

		String template = LaTeXTemplatesService.getInstance().getTemplateMainLaTeXDocument();

		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), input.getSchriftgroesse().getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(),
			input.getSchriftgroesse().getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT.placeholder(), input.getAufgabensammlung().name + " (Lösungen)");

		String content = printContentLoesungen(aufgaben, raetselLaTeX);

		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), content);

		String textLizenzFont = new GeneratorFontsDelegate().getTextLizenzFont(input.getFont());
		template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(), textLizenzFont);

		return template;
	}

	String printContentLoesungen(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX) {

		StringBuffer sb = new StringBuffer();
		int count = 0;

		for (Quizaufgabe aufgabe : aufgaben) {

			Optional<RaetselLaTeXDto> opt = raetselLaTeX.stream().filter(r -> aufgabe.getSchluessel().equals(r.getSchluessel()))
				.findFirst();

			if (opt.isPresent() && StringUtils.isNotBlank(opt.get().getLoesung())) {

				if (count > 0) {

					sb.append(LaTeXConstants.ABSTAND_ITEMS);
				}

				String text = LaTeXConstants.INPUT_LOESUNG;
				text = text.replace(LaTeXPlaceholder.NUMMER.placeholder(), aufgabe.getNummer());
				text = text.replace(LaTeXPlaceholder.SCHLUESSEL.placeholder(), aufgabe.getSchluessel());

				sb.append(text);

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

		}

		return sb.toString();
	}

}
