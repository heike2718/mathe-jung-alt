// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * DeleteUnusedEmbeddableImageFilesService
 */
@ApplicationScoped
public class DeleteUnusedEmbeddableImageFilesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUnusedEmbeddableImageFilesService.class);

	FindPathsGrafikParser pathsParser = new FindPathsGrafikParser();

	@Inject
	RaetselFileService raetselFileService;

	/**
	 * Sucht aus dem Text von frage und loesung des persistenten Raetsels die importierten Grafikdateien, die entfernt wurden und
	 * löscht diese im Filesystem.
	 *
	 * @param raetsel
	 * @param raetselDB
	 */
	public void checkAndDeleteUnusedFiles(final FragenUndLoesungenVO fragenUndLoesungen) {

		List<String> altePfade = pathsParser.findPaths(fragenUndLoesungen.getFrageAlt());
		List<String> neuePfade = pathsParser.findPaths(fragenUndLoesungen.getFrageNeu());

		altePfade.addAll(pathsParser.findPaths(fragenUndLoesungen.getLoesungAlt()));
		neuePfade.addAll(pathsParser.findPaths(fragenUndLoesungen.getLoesungNeu()));

		checkAndDelete(altePfade, neuePfade);
	}

	private void checkAndDelete(final List<String> pfadeNeu, final List<String> pfadeAlt) {

		List<String> pfadeUnused = findUnusedPaths(pfadeNeu, pfadeAlt);

		if (!pfadeUnused.isEmpty()) {

			for (String pfad : pfadeUnused) {

				try {

					boolean deleted = raetselFileService.deleteImageFile(pfad);

					if (deleted) {

						LOGGER.info("{} wurde geloescht.", pfad);
					} else {

						LOGGER.info("{} wurde nicht geloescht. Ist vermutlich nicht mehr da gewesen.", pfad);

					}
				} catch (MjaRuntimeException e) {

					LOGGER.error(e.getMessage());
				}
			}

		}

	}

	private List<String> findUnusedPaths(final List<String> altePfade, final List<String> neuePfade) {

		final List<String> result = new ArrayList<>();

		for (String pfadAlt : altePfade) {

			if (!neuePfade.contains(pfadAlt)) {

				result.add(pfadAlt);
			}
		}

		return result;
	}

}
