// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.quiz.impl.QuizaufgabeComparator;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.utils.GeneratorUtils;

/**
 * KarteiLaTeXGeneratorStrategy
 */
public class KarteiLaTeXGeneratorStrategy implements RaetselgruppeLaTeXGeneratorStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(KarteiLaTeXGeneratorStrategy.class);

	@Override
	public String generateLaTeX(final RaetselgruppeGeneratorInput input, final LaTeXTemplatesService templatesService, final RaetselService raetselService, final QuizitemLaTeXGenerator quizitemLaTeXGenerator) {

		List<Quizaufgabe> aufgaben = input.getAufgaben();
		Collections.sort(aufgaben, new QuizaufgabeComparator());

		String template = templatesService.getTemplatePDFKartei();

		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());

		List<String> schluessel = aufgaben.stream().map(a -> a.getSchluessel()).toList();
		List<RaetselLaTeXDto> raetselLaTeX = raetselService.findRaetselLaTeXwithSchluessel(schluessel);

		int count = 0;

		StringBuffer sb = new StringBuffer();

		for (Quizaufgabe aufgabe : aufgaben) {

			Optional<RaetselLaTeXDto> opt = raetselLaTeX.stream().filter(r -> aufgabe.getSchluessel().equals(r.getSchluessel()))
				.findFirst();

			if (opt.isPresent()) {

				appendFrageLoesung(sb, aufgabe, opt.get(), input.getLayoutAntwortvorschlaege(), input.getVerwendungszweck(),
					quizitemLaTeXGenerator);

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

			if (GeneratorUtils.shouldPrintTrenner(aufgaben.size(), count)) {

				sb.append(LaTeXConstants.VALUE_NEWPAGE);
			}
			count++;

		}

		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), sb.toString());
		return template;
	}

	/**
	 * @param sb
	 * @param raetselLaTeXDto
	 */
	private void appendFrageLoesung(final StringBuffer sb, final Quizaufgabe aufgabe, final RaetselLaTeXDto raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final Verwendungszweck verwendungszweck, final QuizitemLaTeXGenerator quizitemLaTeXGenerator) {

		RaetselGeneratorinput raetselInput = new RaetselGeneratorinput()
			.withAntwortvorschlaege(aufgabe.getAntwortvorschlaege())
			.withFrage(raetsel.getFrage()).withLoesung(raetsel.getLoesung())
			.withLayoutAntwortvorschlaege(layoutAntwortvorschlaege)
			.withNummer(aufgabe.getNummer())
			.withSchluessel(aufgabe.getSchluessel())
			.withVerwendungszweck(verwendungszweck)
			.withPunkten(aufgabe.getPunkte());

		boolean printAsMultipleChoice = GeneratorUtils.shouldPrintAntwortvorschlaege(layoutAntwortvorschlaege, aufgabe);

		String textFrage = quizitemLaTeXGenerator.generateLaTeXFrage(raetselInput, printAsMultipleChoice);
		sb.append(textFrage);

		if (StringUtils.isNotBlank(raetselInput.getLoesung())) {

			sb.append(LaTeXConstants.VALUE_NEWPAGE);
			String textLoesung = quizitemLaTeXGenerator.generateLaTeXLoesung(raetselInput, printAsMultipleChoice);
			sb.append(textLoesung);
		}
	}

}
