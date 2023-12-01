// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.raetsel.dto.EmbeddableImageInfo;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * EmbeddedImagesService
 */
@ApplicationScoped
public class EmbeddedImagesService {

	private final static Logger LOGGER = LoggerFactory.getLogger(EmbeddedImagesService.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@Inject
	AuthenticationContext authCtx;

	@Inject
	RaetselDao raetselDao;

	@Inject
	RaetselService raetselService;

	/**
	 * Gibt alle zum Rätsel gehörenden eingebetteten Grafiken zurück.
	 *
	 * @param  raetselId
	 * @return
	 */
	public List<GeneratedFile> getEmbeddedImages(final String raetselId) {

		PersistentesRaetsel persistentesRaetsel = raetselDao.findById(raetselId);

		if (persistentesRaetsel == null) {

			throw new WebApplicationException(
				Response.status(404).entity(MessagePayload.error("Tja, dieses Rätsel gibt es leider nicht.")).build());
		}

		AuthenticatedUser user = authCtx.getUser();

		if (isNotAllowedToDownloadGraphicsForRaetsel(persistentesRaetsel, user)) {

			LOGGER.warn("User {} versucht, embedded images von Raetsel {} mit owner {} herunterzuladen", user.toString(),
				persistentesRaetsel.schluessel, StringUtils.abbreviate(persistentesRaetsel.owner, 11));

			throw new WebApplicationException(
				Response.status(403).entity(MessagePayload.error("Zugriff auf Resource nicht erlaubt")).build());

		}

		Pair<List<EmbeddableImageInfo>, List<EmbeddableImageInfo>> embeddedImagesInfos = raetselService
			.loadEmbeddableImageInfos(persistentesRaetsel);

		List<EmbeddableImageInfo> imageInfos = embeddedImagesInfos.getLeft();
		imageInfos.addAll(embeddedImagesInfos.getRight());

		Set<EmbeddableImageInfo> existingFiles = imageInfos.stream().filter(ei -> ei.isExistiert())
			.collect(Collectors.toSet());

		List<GeneratedFile> result = new ArrayList<>();

		for (EmbeddableImageInfo existingFile : existingFiles) {

			String path = latexBaseDir + existingFile.getPfad();
			byte[] data = MjaFileUtils.loadBinaryFile(path, false);
			result.add(new GeneratedFile(existingFile.getFilename(), data));

		}

		return result;
	}

	/**
	 * @param  persistentesRaetsel
	 * @param  user
	 * @return
	 */
	private boolean isNotAllowedToDownloadGraphicsForRaetsel(final PersistentesRaetsel persistentesRaetsel, final AuthenticatedUser user) {

		return !persistentesRaetsel.owner.equals(user.getUuid()) && user.getBenutzerart() != Benutzerart.ADMIN;
	}

}
