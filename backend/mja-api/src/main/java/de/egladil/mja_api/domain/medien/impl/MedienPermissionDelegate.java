// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesMedium;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * MedienPermissionDelegate
 */
@ApplicationScoped
public class MedienPermissionDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(MedienPermissionDelegate.class);

	@Inject
	AuthenticationContext authCtx;

	/**
	 * Prüft ob der angemeldete User schreubberechtigt ist.
	 *
	 * @param  ausDB
	 * @throws WebApplicationException
	 *                                 wenn nicht
	 */
	public void checkReadPermission(final PersistentesMedium ausDB) throws WebApplicationException {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM:
		case STANDARD: {

			LOGGER.warn("User {} mit Benutzerart {} hat keine Leseberechtigung für Medium {} mit Owner {}",
				user.getName(), benutzerart, ausDB.uuid,
				ausDB.owner);
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		case AUTOR:
		case ADMIN:
			return;

		default:
			throw new IllegalArgumentException("Unexpected benutzerart: " + benutzerart);
		}
	}

	/**
	 * Prüft ob der angemeldete User schreubberechtigt ist.
	 *
	 * @param  ausDB
	 * @throws WebApplicationException
	 *                                 wenn nicht
	 */
	public void checkWritePermission(final PersistentesMedium ausDB) throws WebApplicationException {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM:
		case STANDARD: {

			LOGGER.warn("User {} mit Benutzerart {} hat keine Schreibberechtigung für Medium {} mit Owner {}",
				user.getName(), benutzerart, ausDB.uuid,
				ausDB.owner);
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		case AUTOR: {

			if (!user.getUuid().equals(ausDB.owner)) {

				LOGGER.warn("Autor {} hat keine Schreibberechtigung für Medium {} mit Owner {}",
					user.getName(), ausDB.uuid,
					ausDB.owner);
				throw new WebApplicationException(Status.FORBIDDEN);
			}
		}

		case ADMIN:
			return;

		default:
			throw new IllegalArgumentException("Unexpected benutzerart: " + benutzerart);
		}
	}
}
