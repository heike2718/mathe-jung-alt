// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import de.egladil.mja_admin_api.domain.sammlungen.dto.Aufgabensammlung;

/**
 * AufgabensammlungService
 */
@ApplicationScoped
public class AufgabensammlungService {

	/**
	 * Sucht alle Aufgaben der durch die Parameter eindeutig bestimmten Sammlung zur Präsentation im Browser.
	 *
	 * @param  referenztyp
	 * @param  referenz
	 * @param  schwierigkeitsgrad
	 * @return                    Optional
	 */
	public Optional<Aufgabensammlung> findAufgabensammlungByUniqueKey(final Referenztyp referenztyp, final String referenz, final Schwierigkeitsgrad schwierigkeitsgrad) {

		return null;
	}

}
