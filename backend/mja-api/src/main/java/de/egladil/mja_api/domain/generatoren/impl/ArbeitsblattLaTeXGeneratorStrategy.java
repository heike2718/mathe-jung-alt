// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.quiz.impl.QuizaufgabeComparator;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.utils.GeneratorUtils;

/**
 * ArbeitsblattLaTeXGeneratorStrategy
 */
public class ArbeitsblattLaTeXGeneratorStrategy implements RaetselgruppeLaTeXGeneratorStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArbeitsblattLaTeXGeneratorStrategy.class);

	@Override
	public String generateLaTeX(final RaetselgruppeGeneratorInput input, final LaTeXTemplatesService templatesService, final RaetselService raetselService, final QuizitemLaTeXGenerator quizitemLaTeXGenerator) {

		List<Quizaufgabe> aufgaben = input.getAufgaben();
		Collections.sort(aufgaben, new QuizaufgabeComparator());

		String template = templatesService.getTemplatePDFAufgabenblatt();

		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT.placeholder(), input.getRaetselgruppe().name);

		List<String> schluessel = aufgaben.stream().map(a -> a.getSchluessel()).toList();
		List<RaetselLaTeXDto> raetselLaTeX = raetselService.findRaetselLaTeXwithSchluessel(schluessel);

		String contentAufgaben = this.printContentAufgaben(aufgaben, raetselLaTeX, input.getVerwendungszweck(),
			quizitemLaTeXGenerator, input);
		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), contentAufgaben);
		return template;
	}

	private String printContentAufgaben(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final Verwendungszweck verwendungszweck, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final RaetselgruppeGeneratorInput input) {

		StringBuffer sb = new StringBuffer();
		int count = 0;

		for (Quizaufgabe aufgabe : aufgaben) {

			Optional<RaetselLaTeXDto> opt = raetselLaTeX.stream().filter(r -> aufgabe.getSchluessel().equals(r.getSchluessel()))
				.findFirst();

			if (opt.isPresent()) {

				RaetselLaTeXDto raetsel = opt.get();

				RaetselGeneratorinput raetselInput = new RaetselGeneratorinput()
					.withAntwortvorschlaege(aufgabe.getAntwortvorschlaege())
					.withFrage(raetsel.getFrage()).withLoesung(raetsel.getLoesung())
					.withLayoutAntwortvorschlaege(input.getLayoutAntwortvorschlaege())
					.withNummer(aufgabe.getNummer())
					.withSchluessel(aufgabe.getSchluessel())
					.withVerwendungszweck(verwendungszweck)
					.withPunkten(aufgabe.getPunkte());

				if (count > 0) {

					sb.append(LaTeXConstants.ABSTAND_ITEMS);
				}

				boolean printAsMultipleChoice = GeneratorUtils.shouldPrintAntwortvorschlaege(input.getLayoutAntwortvorschlaege(),
					aufgabe);

				String textFrage = quizitemLaTeXGenerator.generateLaTeXFrage(raetselInput, printAsMultipleChoice);
				sb.append(textFrage);

				if (GeneratorUtils.shouldPrintTrenner(aufgaben.size(), count)) {

					sb.append(LaTeXConstants.VALUE_LINEBREAK);
				}
				count++;

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

		}

		return sb.toString();
	}

}
