// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.generatoren.impl;

import de.egladil.raetselbaukasten.domain.raetsel.Antwortvorschlag;

/**
 * NoopAntwortvorschlagGeneratorStrategegy
 */
public class NoopAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

    @Override
    public String generateLaTeXAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

        return "";
    }

}
