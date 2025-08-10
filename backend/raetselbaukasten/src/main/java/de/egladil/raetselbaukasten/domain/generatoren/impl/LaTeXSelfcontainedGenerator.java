// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren.impl;

import de.egladil.raetselbaukasten.domain.generatoren.dto.AufgabensammlungGeneratorInput;
import de.egladil.raetselbaukasten.domain.quiz.dto.Quizaufgabe;
import de.egladil.raetselbaukasten.domain.raetsel.dto.RaetselLaTeXDto;

import java.util.List;

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
        template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_AUFGABEN.placeholder(), input.getAufgabensammlung().getName());
        template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT_LOESUNGEN.placeholder(), input.getAufgabensammlung().getName());

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
