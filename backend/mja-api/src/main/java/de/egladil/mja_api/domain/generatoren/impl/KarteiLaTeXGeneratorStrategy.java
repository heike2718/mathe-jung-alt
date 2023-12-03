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

import de.egladil.mja_api.domain.generatoren.TrennerartFrageLoesung;
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
public class KarteiLaTeXGeneratorStrategy implements RaetselgruppeGeneratorStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(KarteiLaTeXGeneratorStrategy.class);

	@Override
	public String generateLaTeX(final RaetselgruppeGeneratorInput input, final RaetselService raetselService, final QuizitemLaTeXGenerator quizitemLaTeXGenerator) {

		List<Quizaufgabe> aufgaben = input.getAufgaben();
		Collections.sort(aufgaben, new QuizaufgabeComparator());

		String template = LaTeXTemplatesService.getInstance().getTemplateDocumentPDFKartei();

		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), input.getSchriftgroesse().getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(),
			input.getSchriftgroesse().getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());

		List<String> schluessel = aufgaben.stream().map(a -> a.getSchluessel()).toList();
		List<RaetselLaTeXDto> raetselLaTeX = raetselService.findRaetselLaTeXwithSchluesselliste(schluessel);

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

	/**
	 * @param sb
	 * @param raetselLaTeXDto
	 */
	void appendFrageLoesung(final StringBuffer sb, final Quizaufgabe aufgabe, final RaetselLaTeXDto raetsel, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final Verwendungszweck verwendungszweck, final QuizitemLaTeXGenerator quizitemLaTeXGenerator) {

		RaetselGeneratorinput raetselInput = new RaetselGeneratorinput()
			.withAntwortvorschlaege(aufgabe.getAntwortvorschlaege())
			.withFrage(raetsel.getFrage()).withLoesung(raetsel.getLoesung())
			.withLayoutAntwortvorschlaege(layoutAntwortvorschlaege)
			.withNummer(aufgabe.getNummer())
			.withSchluessel(aufgabe.getSchluessel())
			.withVerwendungszweck(verwendungszweck)
			.withPunkten(aufgabe.getPunkte());

		boolean printAsMultipleChoice = GeneratorUtils.shouldPrintAntwortvorschlaege(layoutAntwortvorschlaege, aufgabe);

		String text = quizitemLaTeXGenerator.generateLaTeXFrageLoesung(raetselInput, TrennerartFrageLoesung.SEITENUMBRUCH,
			printAsMultipleChoice);
		sb.append(text);
	}
}
