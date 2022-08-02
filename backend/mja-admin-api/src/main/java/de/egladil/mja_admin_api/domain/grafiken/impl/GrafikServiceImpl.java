// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.grafiken.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_admin_api.domain.grafiken.GrafikService;
import de.egladil.mja_admin_api.domain.raetsel.impl.FindPathsGrafikParser;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * GrafikServiceImpl
 */
@ApplicationScoped
public class GrafikServiceImpl implements GrafikService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GrafikServiceImpl.class);

	private final Pattern pattern;

	@Inject
	RaetselFileService fileService;

	/**
	 *
	 */
	public GrafikServiceImpl() {

		pattern = Pattern.compile(FindPathsGrafikParser.REGEXP_GRAFIK);

	}

	@Override
	public MessagePayload findGrafik(final String relativerPfad) {

		if (relativerPfad == null) {

			LOGGER.error("Aufruf ohne relativen Pfad");
			return MessagePayload.error("Aufruf ohne Pfad");
		}

		if (!validPath(relativerPfad)) {

			LOGGER.error("Aufruf mit ungültigem relativen Pfad!");
			return MessagePayload.error("Aufruf mit ungültigem Pfad");
		}

		boolean exists = fileService.existsGrafik(relativerPfad);

		if (!exists) {

			return MessagePayload.warn("Falls der Pfad stimmt, wurde die Grafik noch nicht hochgeladen.");
		}

		return MessagePayload.ok();
	}

	boolean validPath(final String relativerPfad) {

		Matcher matcher = pattern.matcher(relativerPfad);
		return matcher.matches();
	}

}
