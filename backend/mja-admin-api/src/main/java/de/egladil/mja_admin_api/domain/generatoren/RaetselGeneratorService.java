// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren;

import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.Outputformat;
import de.egladil.mja_admin_api.domain.raetsel.dto.GeneratedImages;

/**
 * RaetselGeneratorService
 */
public interface RaetselGeneratorService {

	/**
	 * Generiert den output des Raetsels im gewünschten Format und gibt die Url als String zurück.
	 *
	 * @param  outputformat
	 * @param  raetselUuid
	 * @param  layoutAntwortvorschlaege
	 *                                   AnzeigeAntwortvorschlaegeTyp
	 * @return
	 */
	GeneratedImages produceOutputReaetsel(Outputformat outputformat, String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

}
