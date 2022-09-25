// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import java.util.Optional;

import javax.ws.rs.WebApplicationException;

import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
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

	/**
	 * @param  raetselgruppeID
	 * @return
	 */
	Optional<RaetselgruppeDetails> loadDetails(String raetselgruppeID);

	/**
	 * Legt ein neues Element an
	 *
	 * @param  raetselgruppeID
	 * @param  payload
	 * @return                 RaetselgruppeDetails
	 */
	RaetselgruppeDetails elementAnlegen(String raetselgruppeID, EditRaetselgruppenelementPayload payload);

	/**
	 * Ändert ein vorhandenes Element
	 *
	 * @param  raetselgruppeID
	 * @param  payload
	 * @return                 RaetselgruppeDetails
	 */
	RaetselgruppeDetails elementAendern(String raetselgruppeID, EditRaetselgruppenelementPayload payload);

	/**
	 * Löscht das gegebene Element der Rätselgruppe.
	 *
	 * @param  raetselgruppeID
	 * @param  elementID
	 * @return                 RaetselgruppeDetails
	 */
	RaetselgruppeDetails elementLoeschen(String raetselgruppeID, String elementID);
}
