// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.raetsel;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.auth.session.AuthenticatedUser;
import de.egladil.raetselbaukasten.domain.auth.session.Benutzerart;
import de.egladil.raetselbaukasten.domain.raetsel.dto.GeneratedFile;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.RaetselDao;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistentesRaetsel;

/**
 * RaetselTexteService
 */
@ApplicationScoped
public class RaetselTexteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaetselTexteService.class);

    @Inject
    AuthenticationContext authCtx;

    @Inject
    RaetselDao raetselDao;

    /**
     * Läd die Texte des gegebenen Rätsels in 2 Dateien herunter: schluessel.tex mit
     * dem Text der Frage, schluessel_l.tex mit dem Text der Lösung.
     *
     * @param raetselId
     * @return List
     */
    @SuppressWarnings("resource") // ist false positive, weil der Container die Response schließt.
    public List<GeneratedFile> getTexte(final String raetselId) {

        PersistentesRaetsel persistentesRaetsel = raetselDao.findById(raetselId);

        if (persistentesRaetsel == null) {

            throw new WebApplicationException(Response
                    .status(404)
                    .entity(MessagePayload.error("Tja, dieses Rätsel gibt es leider nicht."))
                    .build());
        }

        if (isNotAllowedToDownloadLaTeXForRaetsel(persistentesRaetsel, authCtx.getUser())) {

            LOGGER
                    .warn("User {} versucht, LaTeX-Texte von Raetsel {} mit owner {} herunterzuladen",
                            authCtx.getUser().toString(), persistentesRaetsel.getSchluessel(),
                            StringUtils.abbreviate(persistentesRaetsel.getOwner(), 11));

            throw new WebApplicationException(
                    Response.status(403).entity(MessagePayload.error("Zugriff auf Resource nicht erlaubt")).build());
        }

        List<GeneratedFile> files = new ArrayList<>();

        files
                .add(new GeneratedFile(persistentesRaetsel.getSchluessel() + ".tex",
                        persistentesRaetsel.getFrage().getBytes()));

        if (StringUtils.isNotBlank(persistentesRaetsel.getLoesung())) {

            files
                    .add(new GeneratedFile(persistentesRaetsel.getSchluessel() + "_l.tex",
                            persistentesRaetsel.getLoesung().getBytes()));

        }

        return files;
    }

    /**
     * @param persistentesRaetsel
     * @param user
     * @return
     */
    private boolean isNotAllowedToDownloadLaTeXForRaetsel(final PersistentesRaetsel persistentesRaetsel,
            final AuthenticatedUser user) {

        return !persistentesRaetsel.getOwner().equals(user.getUuid()) && user.getBenutzerart() != Benutzerart.ADMIN;
    }

}
