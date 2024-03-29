// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * AufgabensammlungPermissionDelegate
 */
@ApplicationScoped
public class AufgabensammlungPermissionDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(AufgabensammlungPermissionDelegate.class);

	@Inject
	AuthenticationContext authCtx;

	/**
	 * Prüft, ob der eingeloggte User Leseberechtigung für die gegebene Aufgabensammlung hat.
	 *
	 * @param ausDB
	 *              PersistenteAufgabensammlung
	 */
	public void checkReadPermission(final PersistenteAufgabensammlung ausDB) throws WebApplicationException, IllegalArgumentException {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM: {

			LOGGER.warn("anonymer User {} hat hat keine Leseberechtigung für Aufgabensammlung {} mit Owner {}",
				user.getName(), ausDB.uuid,
				ausDB.owner);
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		case STANDARD:
			if (readForbiddenForSTANDARD(ausDB, user)) {

				LOGGER.warn("User {} mit Benutzerart {} hat hat keine Leseberechtigung für Aufgabensammlung {} mit Owner {}",
					user.getName(), benutzerart, ausDB.uuid,
					ausDB.owner);
				throw new WebApplicationException(Status.FORBIDDEN);
			}
		case AUTOR:
			if (readForbiddenForAUTOR(ausDB, user)) {

				LOGGER.warn("User {} mit Benutzerart {} hat hat keine Leseberechtigung für Aufgabensammlung {} mit Owner {}",
					user.getName(), benutzerart, ausDB.uuid,
					ausDB.owner);
				throw new WebApplicationException(Status.FORBIDDEN);

			}
		case ADMIN:
			return;

		default:
			throw new IllegalArgumentException("Unexpected benutzerart: " + benutzerart);
		}

	}

	/**
	 * Prüft, ob der eingeloggte User Schreibberechtigung für die gegebene Aufgabensammlung hat.
	 *
	 * @param ausDB
	 *              PersistenteAufgabensammlung
	 */
	public void checkWritePermission(final PersistenteAufgabensammlung ausDB) {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM: {

			LOGGER.warn("anonymer User {} hat keine Schreibberechtigung für Aufgabensammlung {} mit Owner {}",
				user.getName(), ausDB.uuid,
				ausDB.owner);
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		case STANDARD:
		case AUTOR:
			// privat oder nicht privat muss nicht gesondert behandelt werden, da Standarduser
			// nicht owner von public Aufgabensammlungen sein können
			if (!ausDB.owner.equals(user.getUuid())) {

				LOGGER.warn("User {} mit Benutzerart {} hat keine Schreibberechtigung für Aufgabensammlung {} mit Owner {}",
					user.getName(), benutzerart, ausDB.uuid,
					ausDB.owner);
				throw new WebApplicationException("keine Schreibberechtigung für Aufgabensammlung", Status.FORBIDDEN);
			}
		case ADMIN:
			return;

		default:
			throw new IllegalArgumentException("Unexpected benutzerart: " + benutzerart);
		}
	}

	public boolean isSchreibgeschuetztFuerUser(final PersistenteAufgabensammlung ausDB) {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM: {

			LOGGER.warn("anonymer User {} hat keine Schreibberechtigung für Aufgabensammlung {} mit Owner {}",
				user.getName(), ausDB.uuid,
				ausDB.owner);
			return true;
		}

		case STANDARD:
		case AUTOR:
			// privat oder nicht privat muss nicht gesondert behandelt werden, da Standarduser
			// nicht owner von public Aufgabensammlungen sein können
			return !ausDB.owner.equals(user.getUuid());

		case ADMIN:
			return false;

		default:
			throw new IllegalArgumentException("Unexpected benutzerart: " + benutzerart);
		}
	}

	/**
	 * @param  ausDB
	 * @param  user
	 * @return
	 */
	private boolean readForbiddenForSTANDARD(final PersistenteAufgabensammlung ausDB, final AuthenticatedUser user) {

		if (ausDB.privat) {

			return !ausDB.owner.equals(user.getUuid());
		}

		return !ausDB.freigegeben;
	}

	/**
	 * @param  ausDB
	 * @param  user
	 * @return
	 */
	private boolean readForbiddenForAUTOR(final PersistenteAufgabensammlung ausDB, final AuthenticatedUser user) {

		if (ausDB.privat) {

			return !ausDB.owner.equals(user.getUuid());
		}

		return false;
	}
}
