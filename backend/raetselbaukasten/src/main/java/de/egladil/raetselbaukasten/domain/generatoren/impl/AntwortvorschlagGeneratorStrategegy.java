// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren.impl;

import de.egladil.raetselbaukasten.domain.raetsel.Antwortvorschlag;
import de.egladil.raetselbaukasten.domain.raetsel.LayoutAntwortvorschlaege;

public interface AntwortvorschlagGeneratorStrategegy {

    String PATTERN_TABLETYPE = "{*{#}{|C{2cm}}|}";

    /**
     * Generiert den LaTeX-Code für die Antwortvorschläge zum gegebenen
     * antwortvorschlaegeTyp
     *
     * @param antwortVorschlaege Antwortvorschlag[]
     * @return String LaTeX-Code
     */
    String generateLaTeXAntwortvorschlaege(Antwortvorschlag[] antwortVorschlaege);

    static AntwortvorschlagGeneratorStrategegy create(final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

        AntwortvorschlagGeneratorStrategegy result = null;

        switch (layoutAntwortvorschlaege) {

        case NOOP -> result = new NoopAntwortvorschlagGeneratorStrategegy();
        case ANKREUZTABELLE -> result = new AnkreuztabelleAntwortvorschlagGeneratorStrategegy();
        case BUCHSTABEN -> result = new BuchstabenAntwortvorschlagGeneratorStrategegy();
        case DESCRIPTION -> result = new DescriptionAntwortvorschlagGeneratorStrategegy();
        default ->
            throw new IllegalArgumentException("unbekannter LayoutAntwortvorschlaege " + layoutAntwortvorschlaege);
        }

        return result;
    }

}
