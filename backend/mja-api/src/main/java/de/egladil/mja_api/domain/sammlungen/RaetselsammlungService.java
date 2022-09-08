// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.sammlungen;

import de.egladil.mja_api.domain.sammlungen.dto.EditRaetselsammlungPayload;
import de.egladil.mja_api.domain.sammlungen.dto.RaetselsammlungSucheTrefferItem;

/**
 * RaetselsammlungService
 */
public interface RaetselsammlungService {

	/**
	 * Erstellt eine neue Rätselsammlung. Dabei wird geprüft, ob es eine mit den Keys bereits gibt oder dem Namen. In diesem Fall
	 * wird eine
	 * WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  user
	 * @return         RaetselsammlungSucheTrefferItem
	 */
	RaetselsammlungSucheTrefferItem raetselsammlungAnlegen(EditRaetselsammlungPayload payload, String user);

	/**
	 * Ändert die Basisdaten einer Rätselsammlung. Dabei wird geprüft, ob es eine mit den Keys oder dem Namen bereits gibt. In
	 * diesem Fall wird
	 * eine
	 * WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  user
	 * @return         RaetselsammlungSucheTrefferItem
	 */
	RaetselsammlungSucheTrefferItem raetselsammlungBasisdatenAendern(EditRaetselsammlungPayload payload, String user);

}
