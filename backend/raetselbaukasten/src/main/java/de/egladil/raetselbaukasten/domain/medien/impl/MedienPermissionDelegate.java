// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.medien.impl;

import de.egladil.raetselbaukasten.domain.auth.session.AuthenticatedUser;
import de.egladil.raetselbaukasten.domain.auth.session.Benutzerart;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistentesMedium;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param ausDB
     * @throws WebApplicationException wenn nicht
     */
    public void checkReadPermission(final PersistentesMedium ausDB) throws WebApplicationException {

        AuthenticatedUser user = authCtx.getUser();

        Benutzerart benutzerart = user.getBenutzerart();

        switch (benutzerart) {

            case ANONYM:
            case STANDARD: {

                LOGGER.warn("User {} mit Benutzerart {} hat keine Leseberechtigung für Medium {} mit Owner {}",
                        user.getName(), benutzerart, ausDB.getUuid(),
                        ausDB.getOwner());
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
     * @param ausDB
     * @throws WebApplicationException wenn nicht
     */
    public void checkWritePermission(final PersistentesMedium ausDB) throws WebApplicationException {

        AuthenticatedUser user = authCtx.getUser();

        Benutzerart benutzerart = user.getBenutzerart();

        switch (benutzerart) {

            case ANONYM:
            case STANDARD: {

                LOGGER.warn("User {} mit Benutzerart {} hat keine Schreibberechtigung für Medium {} mit Owner {}",
                        user.getName(), benutzerart, ausDB.getUuid(),
                        ausDB.getOwner());
                throw new WebApplicationException(Status.FORBIDDEN);
            }

            case AUTOR: {

                if (!user.getUuid().equals(ausDB.getOwner())) {

                    LOGGER.warn("Autor {} hat keine Schreibberechtigung für Medium {} mit Owner {}",
                            user.getName(), ausDB.getUuid(),
                            ausDB.getOwner());
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
