// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren.impl;

import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.Raetsel;

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
