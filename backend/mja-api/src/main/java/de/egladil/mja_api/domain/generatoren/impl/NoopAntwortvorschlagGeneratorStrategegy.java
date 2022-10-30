// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;

/**
 * NoopAntwortvorschlagGeneratorStrategegy
 */
public class NoopAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

	@Override
	public String generateLaTeXAntwortvorschlaege(final Antwortvorschlag[] antwortvorschlaege) {

		return "";
	}

}
