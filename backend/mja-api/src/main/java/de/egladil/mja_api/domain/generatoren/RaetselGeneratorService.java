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
	 * Generiert den output des Raetsels im gewünschten Format und gibt die Url als String zurück. Nur user mit Änderungsrecht auf
	 * das Rätsel dürfen diese Methode aufrufen.
	 *
	 * @param  raetselUuid
	 *                                  String
	 * @param  layoutAntwortvorschlaege
	 *                                  AnzeigeAntwortvorschlaegeTyp
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @return
	 */
	Images generatePNGsRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font, Schriftgroesse schriftgroesse);

	/**
	 * Generiert das Rätsel als 2seitiges PDF: Frage auf Seite 1, Lösung auf Seite 2. Minderprivilegierte User bekommen nur bei
	 * freigegebenen Rätseln ein PDF.
	 *
	 * @param  raetselUuid
	 * @param  layoutAntwortvorschlaege
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @return                          GeneratedFile
	 */
	GeneratedFile generatePDFRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font, Schriftgroesse schriftgroesse);

}
