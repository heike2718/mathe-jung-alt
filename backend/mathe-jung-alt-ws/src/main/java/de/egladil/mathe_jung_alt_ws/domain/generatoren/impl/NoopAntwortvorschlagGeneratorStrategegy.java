// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

/**
 * NoopAntwortvorschlagGeneratorStrategegy
 */
public class NoopAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

	@Override
	public String generateLaTeXAntwortvorschlaege(final Raetsel raetsel) {

		return "";
	}

}
