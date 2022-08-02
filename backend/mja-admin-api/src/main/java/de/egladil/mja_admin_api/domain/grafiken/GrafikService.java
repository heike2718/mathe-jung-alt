// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.grafiken;

import de.egladil.mja_admin_api.domain.dto.UploadRequestDto;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * GrafikService
 */
public interface GrafikService {

	/**
	 * Prüft, ob es die Grafikdatei gibt.
	 *
	 * @param  relativerPfad
	 * @return               Grafik oder null;
	 */
	MessagePayload findGrafik(String relativerPfad);

	/**
	 * Speichert die Grafik-Datei, sofern sie virenfrei ist und alle anderen Validierungen passen.
	 *
	 * @param  relativerPfad
	 * @param  dto
	 * @return
	 */
	MessagePayload grafikSpeichern(String relativerPfad, UploadRequestDto dto);

}
