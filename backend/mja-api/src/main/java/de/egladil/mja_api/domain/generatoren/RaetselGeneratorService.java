// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.Images;

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
	Images generatePNGsRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege, final String userId, final boolean isAdmin);

	/**
	 * Generiert das Rätsel als 2seitiges PDF.
	 *
	 * @param  raetselUuid
	 * @param  layoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	GeneratedFile generatePDFRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege, final String userId, final boolean isAdmin);

}
