// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.AnzeigeAntwortvorschlaegeTyp;
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

	public static AntwortvorschlagGeneratorStrategegy create(final AnzeigeAntwortvorschlaegeTyp typ) {

		switch (typ) {

		case NOOP:

			return new NoopAntwortvorschlagGeneratorStrategegy();

		case ANKREUZTABELLE:
			return new AnkreuztabelleAntwortvorschlagGeneratorStrategegy();

		case BUCHSTABEN:
			return new BuchstabenAntwortvorschlagGeneratorStrategegy();

		case DESCRIPTION:
			return new DescriptionAntwortvorschlagGeneratorStrategegy();

		default:
			throw new IllegalArgumentException("unbekannter Antwortanzegetyp " + typ);
		}
	}

}
