// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import java.util.Optional;

import javax.ws.rs.WebApplicationException;

import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import de.egladil.web.mja_auth.session.AuthenticatedUser;

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
	 * @param  userId
	 *                 String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                 boolean
	 * @return         RaetselgruppensucheTrefferItem
	 */
	RaetselgruppensucheTrefferItem raetselgruppeAnlegen(EditRaetselgruppePayload payload, AuthenticatedUser user) throws WebApplicationException;

	/**
	 * Ändert die Basisdaten einer Rätselgruppe. Dabei wird geprüft, ob es eine mit den Keys oder dem Namen bereits gibt. In
	 * diesem Fall wird eine WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  userId
	 *                 String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                 boolean
	 * @return         RaetselgruppensucheTrefferItem
	 */
	RaetselgruppensucheTrefferItem raetselgruppeBasisdatenAendern(EditRaetselgruppePayload payload, AuthenticatedUser user) throws WebApplicationException;

	/**
	 * @param  raetselgruppeID
	 * @param  userId
	 *                         String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                         boolean
	 * @return                 Optional
	 */
	Optional<RaetselgruppeDetails> loadDetails(String raetselgruppeID, AuthenticatedUser user);

	/**
	 * Legt ein neues Element an
	 *
	 * @param raetselgruppeID
	 * @param payload
	 * @param userId
	 *                        String die ID des eingeloggten Users
	 * @param isAdmin
	 *                        boolean * @return RaetselgruppeDetails
	 */
	RaetselgruppeDetails elementAnlegen(String raetselgruppeID, EditRaetselgruppenelementPayload payload, AuthenticatedUser user);

	/**
	 * Ändert ein vorhandenes Element
	 *
	 * @param raetselgruppeID
	 * @param payload
	 * @param userId
	 *                        String die ID des eingeloggten Users
	 * @param isAdmin
	 *                        boolean * @return RaetselgruppeDetails
	 */
	RaetselgruppeDetails elementAendern(String raetselgruppeID, EditRaetselgruppenelementPayload payload, AuthenticatedUser user);

	/**
	 * Löscht das gegebene Element der Rätselgruppe.
	 *
	 * @param raetselgruppeID
	 * @param elementID
	 * @param userId
	 *                        String die ID des eingeloggten Users
	 * @param isAdmin
	 *                        boolean * @return RaetselgruppeDetails
	 */
	RaetselgruppeDetails elementLoeschen(String raetselgruppeID, String elementID, AuthenticatedUser user);

	/**
	 * Generiert die Vorschau des Quiz als PDF. Dabei werden Aufgaben und Lösungen gemischt.
	 *
	 * @param  raetselgruppeID
	 * @return
	 */
	GeneratedFile printVorschau(final String raetselgruppeID, final LayoutAntwortvorschlaege layoutAntwortvorschlaege);

	/**
	 * Generiert das LaTeX-File für die Raetselgruppe. Die Grafiken muss man sowieso lokal haben. Sollte sich mit kleineren
	 * Textreplacements lokal compilieren lassen.
	 *
	 * @param  raetselgruppeID
	 * @param  layoutAntwortvorschlaege
	 * @return                          GeneratedFile
	 */
	GeneratedFile downloadLaTeXSource(final String raetselgruppeID, final LayoutAntwortvorschlaege layoutAntwortvorschlaege);
}
