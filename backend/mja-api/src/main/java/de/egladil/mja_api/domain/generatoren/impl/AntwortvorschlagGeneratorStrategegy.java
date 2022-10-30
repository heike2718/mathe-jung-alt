// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;

public interface AntwortvorschlagGeneratorStrategegy {

	String PATTERN_TABLETYPE = "{*{#}{|C{2cm}}|}";

	/**
	 * Generiert den LaTeX-Code für die Antwortvorschläge zum gegebenen antwortvorschlaegeTyp
	 *
	 * @param  antwortVorschlaege
	 *                            Antwortvorschlag[]
	 * @return                    String LaTeX-Code
	 */
	String generateLaTeXAntwortvorschlaege(Antwortvorschlag[] antwortVorschlaege);

	public static AntwortvorschlagGeneratorStrategegy create(final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		AntwortvorschlagGeneratorStrategegy result = null;

		switch (layoutAntwortvorschlaege) {

			case NOOP -> result = new NoopAntwortvorschlagGeneratorStrategegy();
			case ANKREUZTABELLE -> result = new AnkreuztabelleAntwortvorschlagGeneratorStrategegy();
			case BUCHSTABEN -> result = new BuchstabenAntwortvorschlagGeneratorStrategegy();
			case DESCRIPTION -> result = new DescriptionAntwortvorschlagGeneratorStrategegy();
			default -> throw new IllegalArgumentException("unbekannter Antwortanzegetyp " + layoutAntwortvorschlaege);
		}

		return result;
	}

}
