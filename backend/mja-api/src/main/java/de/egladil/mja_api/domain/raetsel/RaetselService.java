// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.List;
import java.util.Optional;

import de.egladil.mja_api.domain.dto.AnzahlabfrageResponseDto;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;

/**
 * RaetselService
 */
public interface RaetselService {

	/**
	 * @return AnzahlabfrageResponseDto
	 */
	AnzahlabfrageResponseDto zaehleFreigegebeneRaetsel();

	/**
	 * Sucht alle Rätsel, die zum Suchfilter passen und gibt sie der Permission enstprechend zurück.
	 *
	 * @param  suchfilter
	 * @param  limit
	 *                       int Anzahl Treffer in page
	 * @param  offset
	 *                       int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                       SortDirection nach schluessel
	 * @return               RaetselsucheTreffer
	 */
	RaetselsucheTreffer sucheRaetsel(Suchfilter suchfilter, final int limit, final int offset, SortDirection sortDirection);

	/**
	 * Legt ein neues Rätsel an. Durfen nur Autoren und Admins.
	 *
	 * @param  payload
	 *                 EditRaetselPayload die Daten und Metainformationen
	 * @return         RaetselPayloadDaten mit einer generierten UUID.
	 */
	Raetsel raetselAnlegen(EditRaetselPayload payload);

	/**
	 * Ändert ein vorhandenes Raetsel
	 *
	 * @param  payload
	 *                 EditRaetselPayload die Daten und Metainformationen
	 * @return         RaetselPayloadDaten mit einer generierten UUID.
	 */
	Raetsel raetselAendern(EditRaetselPayload payload);

	/**
	 * Holt die Details des Rätsels zu der gegebenen id. Falls das Rätsel existiert, wird der Schreibschutz anhand der Permissions
	 * für den User aufgehoben.
	 *
	 * @param  id
	 * @return    Raetsel oder null.
	 */
	Raetsel getRaetselZuId(String id);

	/**
	 * @param  schluessel
	 * @return            Optional
	 */
	Optional<String> getRaetselIdWithSchluessel(String schluessel);

	/**
	 * @param  schluessel
	 *                    String
	 * @return            Optional
	 */
	Images findImagesZuSchluessel(String schluessel);

	/**
	 * Läd das, was als Input für ein LaTeX-File erforderlich ist.
	 *
	 * @param  schluessel
	 *                    List eine Liste von Schlüsseln.
	 * @return            List
	 */
	List<RaetselLaTeXDto> findRaetselLaTeXwithSchluessel(List<String> schluessel);
}
