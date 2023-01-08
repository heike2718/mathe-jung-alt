// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.web.mja_auth.session.AuthenticatedUser;

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
	 * @param  user
	 *                                  AuthenticatedUser
	 * @return
	 */
	Images generatePNGsRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege, final AuthenticatedUser user);

	/**
	 * Generiert das Rätsel als 2seitiges PDF: Frage auf Seite 1, Lösung auf Seite 2. Minderprivilegierte User bekommen nur bei
	 * freigegebenen Rätseln ein PDF.
	 *
	 * @param  raetselUuid
	 * @param  layoutAntwortvorschlaege
	 * @param  user
	 * @return                          GeneratedFile
	 */
	GeneratedFile generatePDFRaetsel(String raetselUuid, LayoutAntwortvorschlaege layoutAntwortvorschlaege, final AuthenticatedUser user);

}
