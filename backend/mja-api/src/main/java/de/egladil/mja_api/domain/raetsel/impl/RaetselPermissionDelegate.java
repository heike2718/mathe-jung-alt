// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * RaetselPermissionDelegate testet Zeug mit permissions für Raetsel.
 */
@ApplicationScoped
public class RaetselPermissionDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselPermissionDelegate.class);

	@Inject
	AuthenticationContext authCtx;

	public boolean isOnlyReadFreigegebene() {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM: {

			LOGGER.warn("User {} mit Benutzerart {} hat keine Leseberechtigung auf Raetsel",
				user.getName(),
				benutzerart);
		}

		case STANDARD:
			return true;

		case AUTOR:
		case ADMIN:
			return false;

		default:
			throw new IllegalArgumentException("Unexpected benutzerart: " + benutzerart);
		}

	}

	/**
	 * @param ausDB
	 */
	public void checkWritePermission(final PersistentesRaetsel ausDB) {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM:
		case STANDARD: {

			LOGGER.warn("User {} mit Benutzerart {} hat keine Schreibberechtigung auf Raetsel {} mit Owner {}",
				user.getName(),
				benutzerart, ausDB.schluessel,
				ausDB.owner);
			throw new WebApplicationException("keine Schreibberechtigung für Rätsel", Status.FORBIDDEN);
		}

		case AUTOR: {

			if (!user.getUuid().equals(ausDB.owner)) {

				LOGGER.warn("Autor {} hat keine Schreibberechtigung auf Raetsel {} mit Owner {}",
					user.getName(), ausDB.schluessel,
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

	public boolean isSchreibgeschuetztForUser(final PersistentesRaetsel ausDB) {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM: {

			LOGGER.warn("User {} mit Benutzerart {} hat keine Schreibberechtigung auf Raetsel {} mit Owner {}",
				user.getName(),
				benutzerart, ausDB.schluessel,
				ausDB.owner);
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		case STANDARD:
			return true;

		case AUTOR:
			return !user.getUuid().equals(ausDB.owner);

		case ADMIN:
			return false;

		default:
			throw new IllegalArgumentException("Unexpected benutzerart: " + benutzerart);
		}

	}

	public void checkReadPermission(final Raetsel raetsel) {

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM: {

			LOGGER.warn("anonymer User {} hat keine Leseberechtigung auf Raetsel mit SCHLUESSEL={}",
				user.getName(),
				raetsel.getSchluessel());

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		case STANDARD: {

			if (!raetsel.isFreigegeben()) {

				LOGGER.warn("Standarduser {} hat keine Leseberechtigung auf nicht freigegebenes Raetsel mit SCHLUESSEL={}",
					user.getName(),
					raetsel.getSchluessel());

				throw new WebApplicationException(Status.FORBIDDEN);
			}
		}

		case AUTOR:
		case ADMIN:
			return;

		default:
			throw new IllegalArgumentException("Unexpected value: " + benutzerart);
		}

	}

	/**
	 * @param raetsel
	 */
	public void checkReadPermission(final RaetselsucheTrefferItem raetsel) {

		LOGGER.debug("check the read permission for {}", raetsel.getSchluessel());

		AuthenticatedUser user = authCtx.getUser();

		Benutzerart benutzerart = user.getBenutzerart();

		switch (benutzerart) {

		case ANONYM: {

			LOGGER.warn("anonymer User {} hat keine Leseberechtigung auf Raetsel mit SCHLUESSEL={}",
				user.getName(),
				raetsel.getSchluessel());

			throw new WebApplicationException(Status.FORBIDDEN);
		}

		case STANDARD: {

			if (!raetsel.isFreigegeben()) {

				LOGGER.warn("Standarduser {} hat keine Leseberechtigung auf nicht freigegebenes Raetsel mit SCHLUESSEL={}",
					user.getName(),
					raetsel.getSchluessel());

				throw new WebApplicationException(Status.FORBIDDEN);
			}
		}

		case AUTOR:
		case ADMIN:
			return;

		default:
			throw new IllegalArgumentException("Unexpected value: " + benutzerart);
		}

	}
}
