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
 * AufgabenLoesungenLaTeXGeneratorStrategy
 */
public class AufgabenLoesungenLaTeXGeneratorStrategy implements RaetselgruppeLaTeXGeneratorStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(AufgabenLoesungenLaTeXGeneratorStrategy.class);

	@Override
	public String generateLaTeX(final RaetselgruppeGeneratorInput input, final RaetselService raetselService, final QuizitemLaTeXGenerator quizitemLaTeXGenerator) {

		List<Quizaufgabe> aufgaben = input.getAufgaben();
		Collections.sort(aufgaben, new QuizaufgabeComparator());

		String template = LaTeXTemplatesService.getInstance().getTemplateDocumentPDFAufgabenblattMitLoesungen();

		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), input.getSchriftgroesse().getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(),
			input.getSchriftgroesse().getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_AUFGABEN.placeholder(), input.getRaetselgruppe().name);
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_LOESUNGEN.placeholder(), input.getRaetselgruppe().name);

		List<String> schluessel = aufgaben.stream().map(a -> a.getSchluessel()).toList();
		List<RaetselLaTeXDto> raetselLaTeX = raetselService.findRaetselLaTeXwithSchluessel(schluessel);

		String contentAufgaben = this.printContentAufgaben(aufgaben, raetselLaTeX, input.getVerwendungszweck(),
			quizitemLaTeXGenerator, input);
		String contentLoesungen = this.printContentLoesungen(aufgaben, raetselLaTeX, input.getVerwendungszweck(),
			quizitemLaTeXGenerator, input);

		template = template.replace(LaTeXPlaceholder.CONTENT_FRAGE.placeholder(), contentAufgaben);
		template = template.replace(LaTeXPlaceholder.TRENNER_FRAGE_LOESUNG.placeholder(), LaTeXConstants.VALUE_NEWPAGE);
		template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), contentLoesungen);

		switch (input.getFont()) {

		case DRUCK_BY_WOK:

			template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(),
				LaTeXTemplatesService.getInstance().getLizenzFontsDruckschrift());
			break;

		case FIBEL_NORD:
		case FIBEL_SUED:
			template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(),
				LaTeXTemplatesService.getInstance().getLizenzFontsFibel());
			break;

		case STANDARD:
			template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(), "");
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + input.getFont());
		}

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

				String text = quizitemLaTeXGenerator.generateLaTeXFrage(raetselInput, printAsMultipleChoice);
				sb.append(text);
				count++;

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

		}

		return sb.toString();
	}

	private String printContentLoesungen(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final Verwendungszweck verwendungszweck, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final RaetselgruppeGeneratorInput input) {

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

				String text = quizitemLaTeXGenerator.generateLaTeXLoesung(raetselInput, printAsMultipleChoice);
				sb.append(text);

				if (GeneratorUtils.shouldPrintTrenner(aufgaben.size(), count)) {

					sb.append(LaTeXConstants.ABSTAND_ITEMS);
				}
				count++;

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}

		}

		return sb.toString();
	}

}
