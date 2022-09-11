// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import javax.ws.rs.WebApplicationException;

import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;

/**
 * RaetselgruppenService
 */
public interface RaetselgruppenService {

	RaetselgruppensucheTreffer findRaetselgruppen(RaetselgruppenSuchparameter suchparameter, int limit, int offset);

	/**
	 * Erstellt eine neue Rätselgruppe. Dabei wird geprüft, ob es eine mit den Keys bereits gibt oder dem Namen. In diesem Fall
	 * wird eine
	 * WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  user
	 * @return         RaetselgruppensucheTrefferItem
	 */
	RaetselgruppensucheTrefferItem raetselgruppeAnlegen(EditRaetselgruppePayload payload, String user) throws WebApplicationException;

	/**
	 * Ändert die Basisdaten einer Rätselgruppe. Dabei wird geprüft, ob es eine mit den Keys oder dem Namen bereits gibt. In
	 * diesem Fall wird eine WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  user
	 * @return         RaetselgruppensucheTrefferItem
	 */
	RaetselgruppensucheTrefferItem raetselgruppeBasisdatenAendern(EditRaetselgruppePayload payload, String user) throws WebApplicationException;

}
