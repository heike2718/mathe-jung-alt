// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

public interface AntwortvorschlagGeneratorStrategegy {

	String PATTERN_TABLETYPE = "{*{#}{|C{2cm}}|}";

	/**
	 * Generiert den LaTeX-Code für die Antwortvorschläge zum gegebenen antwortvorschlaegeTyp
	 *
	 * @param  raetsel
	 *                 Raetsel
	 * @return         String LaTeX-Code
	 */
	String generateLaTeXAntwortvorschlaege(Raetsel raetsel);

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
