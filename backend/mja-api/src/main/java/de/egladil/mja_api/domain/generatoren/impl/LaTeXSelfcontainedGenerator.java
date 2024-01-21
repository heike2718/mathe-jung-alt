// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.List;

import de.egladil.mja_api.domain.generatoren.dto.AufgabensammlungGeneratorInput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;

/**
 * LaTeXSelfcontainedGenerator. Generiert das LaTeX für ein vollständig expandiertes LaTex, in dem also Fragen und Lösungen nicht
 * importiert werden.
 */
public class LaTeXSelfcontainedGenerator implements LaTeXDocGeneratorStrategy {

	private final AufgabenLoesungenLaTeXGeneratorDelegate delegate = new AufgabenLoesungenLaTeXGeneratorDelegate();

	@Override
	public String generateLaTeX(final List<Quizaufgabe> aufgaben, final List<RaetselLaTeXDto> raetselLaTeX, final QuizitemLaTeXGenerator quizitemLaTeXGenerator, final AufgabensammlungGeneratorInput input) {

		String template = LaTeXTemplatesService.getInstance().getTemplateDocumentPDFAufgabenblattMitLoesungen();

		template = template.replace(LaTeXPlaceholder.ARRAYSTRETCH.placeholder(), input.getSchriftgroesse().getArrayStretch());
		template = template.replace(LaTeXPlaceholder.SCHRIFTGROESSE.placeholder(),
			input.getSchriftgroesse().getLaTeXReplacement());
		template = template.replace(LaTeXPlaceholder.FONT_NAME.placeholder(), input.getFont().getLatexFileInputDefinition());
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_AUFGABEN.placeholder(), input.getAufgabensammlung().name);
		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_LOESUNGEN.placeholder(), input.getAufgabensammlung().name);

		String contentAufgaben = delegate.printContentAufgaben(aufgaben, raetselLaTeX, quizitemLaTeXGenerator, input);
		String contentLoesungen = delegate.printContentLoesungen(aufgaben, raetselLaTeX, quizitemLaTeXGenerator, input);

		template = template.replace(LaTeXPlaceholder.CONTENT_FRAGE.placeholder(), contentAufgaben);
		template = template.replace(LaTeXPlaceholder.TRENNER_FRAGE_LOESUNG.placeholder(), LaTeXConstants.VALUE_NEWPAGE);
		template = template.replace(LaTeXPlaceholder.CONTENT_LOESUNG.placeholder(), contentLoesungen);

		String textLizenzFont = new GeneratorFontsDelegate().getTextLizenzFont(input.getFont());
		template = template.replace(LaTeXPlaceholder.LIZENZ_FONTS.placeholder(), textLizenzFont);

		String quellen = new QuellenverzeichnisLaTeXGenerator().generiereQuellenverzeichnis(aufgaben);
		template = template.replace(LaTeXPlaceholder.QUELLEN.placeholder(), quellen);

		return template;
	}

}
