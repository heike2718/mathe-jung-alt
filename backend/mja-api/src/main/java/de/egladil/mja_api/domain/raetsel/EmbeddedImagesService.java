// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.ArrayList;
import java.util.List;

import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * EmbeddedImagesService
 */
@ApplicationScoped
public class EmbeddedImagesService {

	@Inject
	AuthenticationContext authCtx;

	/**
	 * Läd alle zum Rätsel gehörenden eingebetteten Grafiken herunter.
	 *
	 * @param  raetselId
	 * @return
	 */
	public List<GeneratedFile> getEmbeddedImages(final String raetselId) {

		return new ArrayList<>();
	}

}
