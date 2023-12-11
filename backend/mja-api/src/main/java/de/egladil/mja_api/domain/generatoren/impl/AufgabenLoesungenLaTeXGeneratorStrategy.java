// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.Collections;
import java.util.List;

import de.egladil.mja_api.domain.generatoren.dto.AufgabensammlungGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.quiz.impl.QuizaufgabeComparator;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;

/**
 * AufgabenLoesungenLaTeXGeneratorStrategy
 */
public class AufgabenLoesungenLaTeXGeneratorStrategy implements AufgabensammlungGeneratorStrategy {

	private final AufgabenLoesungenLaTeXGeneratorDelegate delegate = new AufgabenLoesungenLaTeXGeneratorDelegate();

	@Override
	public String generateLaTeX(final AufgabensammlungGeneratorInput input, final RaetselService raetselService, final QuizitemLaTeXGenerator quizitemLaTeXGenerator) {

		List<Quizaufgabe> aufgaben = input.getAufgaben();
		Collections.sort(aufgaben, new QuizaufgabeComparator());

		String template = LaTeXTemplatesService.getInstance().getTemplateDocumentPDFAufgabenblattMitLoesungen();

		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), input.getSchriftgroesse().getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(),
			input.getSchriftgroesse().getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_AUFGABEN.placeholder(), input.getAufgabensammlung().name);
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_LOESUNGEN.placeholder(), input.getAufgabensammlung().name);

		List<String> schluessel = aufgaben.stream().map(a -> a.getSchluessel()).toList();
		List<RaetselLaTeXDto> raetselLaTeX = raetselService.findRaetselLaTeXwithSchluesselliste(schluessel);

		String contentAufgaben = delegate.printContentAufgaben(aufgaben, raetselLaTeX, quizitemLaTeXGenerator, input);
		String contentLoesungen = delegate.printContentLoesungen(aufgaben, raetselLaTeX, quizitemLaTeXGenerator, input);

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

}
