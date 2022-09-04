// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren;

import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.dto.GeneratedImages;
import de.egladil.mja_admin_api.domain.raetsel.dto.GeneratedPDF;

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
	 *                                  AnzeigeAntwortvorschlaegeTyp
	 * @return
	 */
	GeneratedImages generatePNGsRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

	/**
	 * Generiert das Rätsel als 2seitiges PDF.
	 *
	 * @param  raetselUuid
	 * @param  layoutAntwortvorschlaege
	 * @return                          GeneratedPDF
	 */
	GeneratedPDF generatePDFRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

}
