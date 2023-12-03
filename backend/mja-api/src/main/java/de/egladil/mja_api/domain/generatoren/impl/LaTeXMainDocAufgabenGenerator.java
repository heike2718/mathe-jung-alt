// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.utils.GeneratorUtils;

/**
 * LaTeXMainDocAufgabenGenerator generiert ein LaTeX-File, in dem tex-Files der Aufgabeb mittels input integriert sind. Die
 * Antwortvorschläge, sofern es sie gibt, werden mit dem Layouttyp aus input integriert.
 */
public class LaTeXMainDocAufgabenGenerator implements LaTeXDocGeneratorStrategy {

	private static final String AUFGABENTRENNER = "\n\n% ==================================================================\n\n";

	private static final Logger LOGGER = LoggerFactory.getLogger(LaTeXMainDocAufgabenGenerator.class);

	@Override
	public String generateLaTeX(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final RaetselgruppeGeneratorInput input) {

		LOGGER.warn("Font={}", input.getFont());

		String template = LaTeXTemplatesService.getInstance().getTemplateMainLaTeXDocument();

		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), input.getSchriftgroesse().getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(),
			input.getSchriftgroesse().getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT.placeholder(), input.getRaetselgruppe().name + " (Aufgaben)");

		String content = printContentAufgaben(aufgaben, raetselLaTeX, input);

		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), content);

		String textLizenzFont = new GeneratorFontsDelegate().getTextLizenzFont(input.getFont());
		template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(), textLizenzFont);

		return template;
	}

	String printContentAufgaben(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final RaetselgruppeGeneratorInput input) {

		AntwortvorschlagGeneratorStrategegy antwortvorschlaegeGeneratorStrategy = AntwortvorschlagGeneratorStrategegy
			.create(input.getLayoutAntwortvorschlaege());

		StringBuffer sb = new StringBuffer();
		int count = 0;

		for (Quizaufgabe aufgabe : aufgaben) {

			Optional<RaetselLaTeXDto> opt = raetselLaTeX.stream().filter(r -> aufgabe.getSchluessel().equals(r.getSchluessel()))
				.findFirst();

			if (opt.isPresent()) {

				if (count > 0) {

					sb.append(LaTeXConstants.ABSTAND_ITEMS);
				}

				boolean printAsMultipleChoice = GeneratorUtils.shouldPrintAntwortvorschlaege(input.getLayoutAntwortvorschlaege(),
					aufgabe);

				String text = LaTeXConstants.INPUT_AUFGABE;
				text = text.replace(LaTeXPlaceholder.NUMMER.placeholder(), aufgabe.getNummer());
				text = text.replace(LaTeXPlaceholder.SCHLUESSEL.placeholder(), aufgabe.getSchluessel());

				if (printAsMultipleChoice) {

					text = text
						+ antwortvorschlaegeGeneratorStrategy.generateLaTeXAntwortvorschlaege(aufgabe.getAntwortvorschlaege());
				}

				sb.append(text);
				sb.append(AUFGABENTRENNER);

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

		}

		return sb.toString();
	}
}
