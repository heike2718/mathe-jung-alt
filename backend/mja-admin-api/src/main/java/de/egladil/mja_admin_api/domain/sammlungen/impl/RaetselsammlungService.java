// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.impl;

import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_admin_api.domain.sammlungen.dto.EditRaetselsammlungPayload;

/**
 * RaetselsammlungService
 */
public interface RaetselsammlungService {

	/**
	 * Erstellt eine neue Rätselsammlung. Dabei wird geprüft, ob es eine mit den Keys bereits gibt. In diesem Fall wird eine
	 * WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  user
	 * @return         RaetselSucheTrefferItem
	 */
	RaetselsucheTrefferItem raetselsammlungAnlegen(EditRaetselsammlungPayload payload, String user);

}
