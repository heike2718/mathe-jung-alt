// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

import java.util.Optional;

import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import jakarta.ws.rs.WebApplicationException;

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
	RaetselgruppensucheTrefferItem raetselgruppeAnlegen(EditRaetselgruppePayload payload) throws WebApplicationException;

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
	RaetselgruppensucheTrefferItem raetselgruppeBasisdatenAendern(EditRaetselgruppePayload payload) throws WebApplicationException;

	/**
	 * @param  raetselgruppeID
	 * @param  userId
	 *                         String die ID des eingeloggten Users
	 * @param  isAdmin
	 *                         boolean
	 * @return                 Optional
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

	/**
	 * Generiert die Vorschau des Quiz als PDF. Dabei werden Aufgaben und Lösungen gemischt.
	 * Bei Aufgaben ohne Antwortvorschläge wird keine Tabelle gedruckt.
	 *
	 * @param  raetselgruppeID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 * @return
	 */
	GeneratedFile printVorschau(final String raetselgruppeID, final FontName font, Schriftgroesse schriftgroesse, final LayoutAntwortvorschlaege layoutAntwortvorschlaege);

	/**
	 * Generiert eine Kartei. Für jedes Element wird auf eine Seite die Frage gedruckt, auf die folgende Seite die Lösung.
	 *
	 * @param  raetselgruppeID
	 * @param  font
	 * @param  schriftgroesse
	 *                                  TODO
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege
	 *                                  FontName
	 * @return                          GeneratedFile
	 */
	GeneratedFile printKartei(final String raetselgruppeID, final FontName font, Schriftgroesse schriftgroesse);

	/**
	 * Generiert eine PDF-Datei mit Aufgabenblättern und Lösungen. Zuerst kommen die Aufgaben, danach, beginnend mit einer neuen
	 * Seite, die Lösungen in der gewünschten Reihenfolge. Es wird generell ohne Auntwortvorschläge gedruckt.
	 *
	 * @param  raetselgruppeID
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @return                          GeneratedFile
	 */
	GeneratedFile printArbeitsblattMitLoesungen(final String raetselgruppeID, final FontName font, Schriftgroesse schriftgroesse);

	/**
	 * Generiert das LaTeX-File für die Raetselgruppe. Die Grafiken muss man sowieso lokal haben. Sollte sich mit kleineren
	 * Textreplacements lokal compilieren lassen.
	 *
	 * @param  raetselgruppeID
	 * @return                 GeneratedFile
	 */
	GeneratedFile downloadLaTeXSource(final String raetselgruppeID, final LayoutAntwortvorschlaege layoutAntwortvorschlaege);
}
