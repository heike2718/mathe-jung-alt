// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren.impl;

import de.egladil.mja_admin_api.domain.raetsel.Raetsel;

/**
 * NoopAntwortvorschlagGeneratorStrategegy
 */
public class NoopAntwortvorschlagGeneratorStrategegy implements AntwortvorschlagGeneratorStrategegy {

	@Override
	public String generateLaTeXAntwortvorschlaege(final Raetsel raetsel) {

		return "";
	}

}
