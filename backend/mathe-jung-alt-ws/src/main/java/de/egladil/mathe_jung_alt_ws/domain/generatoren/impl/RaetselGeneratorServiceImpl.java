// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.generatoren.impl;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mathe_jung_alt_ws.domain.dto.Message;
import de.egladil.mathe_jung_alt_ws.domain.error.MjaRuntimeException;
import de.egladil.mathe_jung_alt_ws.domain.generatoren.RaetselFileService;
import de.egladil.mathe_jung_alt_ws.domain.generatoren.RaetselGeneratorService;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.AnzeigeAntwortvorschlaegeTyp;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.RaetselService;
import de.egladil.mathe_jung_alt_ws.infrastructure.restclient.LaTeXRestClient;

/**
 * RaetselGeneratorServiceImpl
 */
@ApplicationScoped
public class RaetselGeneratorServiceImpl implements RaetselGeneratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselGeneratorServiceImpl.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@RestClient
	@Inject
	LaTeXRestClient laTeXClient;

	@Inject
	RaetselService raetselService;

	@Inject
	RaetselFileService raetselFileService;

	@Override
	public String produceOutputReaetsel(final Outputformat outputformat, final String raetselUuid,
			final AnzeigeAntwortvorschlaegeTyp anzeigeAntwortvorschlaege) {

		LOGGER.info("start generate output");

		Raetsel raetsel = raetselService.getRaetselZuId(raetselUuid);

		if (raetsel == null) {

			throw new WebApplicationException(Response.status(404).build());
		}

		raetselFileService.generateFrageLaTeX(raetsel, outputformat, anzeigeAntwortvorschlaege);

		Response response = null;
		LOGGER.info("vor Aufruf LaTeXRestClient");

		try {

			switch (outputformat) {

				case PDF:

					response = laTeXClient.latex2PDF(raetsel.getSchluessel());
					break;

				case PNG:
					response = laTeXClient.latex2PNG(raetsel.getSchluessel());
					break;

				default:
					throw new IllegalArgumentException("unbekanntes outputformat " + outputformat);
			}

			LOGGER.info("nach Aufruf LaTeXRestClient");
			Message message = response.readEntity(Message.class);

			if (message.isOk()) {
				String filename = raetsel.getSchluessel() + outputformat.getFilenameExtension();
				return output2Url(filename);
			}

			LOGGER.error("Mist: generieren hat nicht geklappt: " + message.getMessage());
			throw new MjaRuntimeException(message.getMessage());

		} catch (Exception e) {

			String msg = "Beim generieren des Outputs " + outputformat + " zu Raetsel [schluessel="
					+ raetsel.getSchluessel()
					+ ", uuid=" + raetselUuid + "] ist ein Fehler aufgetreten: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new MjaRuntimeException(msg, e);

		}

	}

	String output2Url(String filename) {

		return "file://" + latexBaseDir + File.separator + filename;
	}

}
