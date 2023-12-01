// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

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
	 * Läd die Texte des gegebenen Rätsels in 2 Dateien herunter: schluessel.tex mit dem Text der Frage, schluessel_l.tex mit dem
	 * Text der Lösung.
	 *
	 * @param  raetselId
	 * @return           List
	 */
	public List<GeneratedFile> getTexte(final String raetselId) {

		PersistentesRaetsel persistentesRaetsel = raetselDao.findById(raetselId);

		if (persistentesRaetsel == null) {

			throw new WebApplicationException(
				Response.status(404).entity(MessagePayload.error("Tja, dieses Rätsel gibt es leider nicht.")).build());
		}

		if (isNotAllowedToDownloadLaTeXForRaetsel(persistentesRaetsel, authCtx.getUser())) {

			LOGGER.warn("User {} versucht, LaTeX-Texte von Raetsel {} mit owner {} herunterzuladen",
				authCtx.getUser().toString(),
				persistentesRaetsel.schluessel, StringUtils.abbreviate(persistentesRaetsel.owner, 11));

			throw new WebApplicationException(
				Response.status(403).entity(MessagePayload.error("Zugriff auf Resource nicht erlaubt")).build());
		}

		List<GeneratedFile> files = new ArrayList<>();

		files.add(new GeneratedFile(persistentesRaetsel.schluessel + ".tex", persistentesRaetsel.frage.getBytes()));

		if (StringUtils.isNotBlank(persistentesRaetsel.loesung)) {

			files.add(new GeneratedFile(persistentesRaetsel.schluessel + "_l.tex", persistentesRaetsel.loesung.getBytes()));

		}

		return files;
	}

	/**
	 * @param  persistentesRaetsel
	 * @param  user
	 * @return
	 */
	private boolean isNotAllowedToDownloadLaTeXForRaetsel(final PersistentesRaetsel persistentesRaetsel, final AuthenticatedUser user) {

		return !persistentesRaetsel.owner.equals(user.getUuid()) && user.getBenutzerart() != Benutzerart.ADMIN;
	}

}
