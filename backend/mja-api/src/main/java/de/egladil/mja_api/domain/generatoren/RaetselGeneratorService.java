// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;

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
	Images generatePNGsRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

	/**
	 * Generiert das Rätsel als 2seitiges PDF.
	 *
	 * @param  raetselUuid
	 * @param  layoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	GeneratedFile generatePDFRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege);

}
