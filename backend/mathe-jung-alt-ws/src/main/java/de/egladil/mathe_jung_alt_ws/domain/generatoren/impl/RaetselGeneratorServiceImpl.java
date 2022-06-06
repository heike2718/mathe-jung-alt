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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mathe_jung_alt_ws.domain.dto.Message;
import de.egladil.mathe_jung_alt_ws.domain.error.MjaRuntimeException;
import de.egladil.mathe_jung_alt_ws.domain.generatoren.RaetselFileService;
import de.egladil.mathe_jung_alt_ws.domain.generatoren.RaetselGeneratorService;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.RaetselService;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.GeneratedImages;
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
	public GeneratedImages produceOutputReaetsel(final Outputformat outputformat, final String raetselUuid, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		LOGGER.debug("start generate output");

		Raetsel raetsel = raetselService.getRaetselZuId(raetselUuid);

		if (raetsel == null) {

			throw new WebApplicationException(
				Response.status(404).entity(Message.error("Es gibt kein Raetsel mit dieser UUID")).build());
		}

		raetselFileService.generateFrageLaTeX(raetsel, layoutAntwortvorschlaege);

		boolean generateLoesung = StringUtils.isNotBlank(raetsel.getLoesung());

		if (generateLoesung) {

			raetselFileService.generateLoesungLaTeX(raetsel);
		}

		Response responseFrage = null;
		Response responseLoesung = null;
		LOGGER.debug("vor Aufruf LaTeXRestClient");

		try {

			switch (outputformat) {

			case PDF: {

				responseFrage = laTeXClient.latex2PDF(raetsel.getSchluessel());

				if (generateLoesung) {

					responseLoesung = laTeXClient.latex2PDF(raetsel.getSchluessel() + "_l");
				}
			}

				break;

			case PNG: {

				responseFrage = laTeXClient.latex2PNG(raetsel.getSchluessel());

				if (generateLoesung) {

					responseLoesung = laTeXClient.latex2PNG(raetsel.getSchluessel() + "_l");
				}
			}
				break;

			default:
				throw new IllegalArgumentException("unbekanntes outputformat " + outputformat);
			}

			LOGGER.debug("nach Aufruf LaTeXRestClient");
			Message message = responseFrage.readEntity(Message.class);

			if (message.isOk()) {

				String filename = raetsel.getSchluessel() + outputformat.getFilenameExtension();

				byte[] imageFrage = this.raetselFileService.findImageFrage(raetsel.getSchluessel());

				GeneratedImages result = new GeneratedImages();
				result.setOutputFormat(outputformat);
				result.setImageFrage(imageFrage);
				result.setUrlFrage(output2Url(filename));

				if (responseLoesung != null) {

					message = responseLoesung.readEntity(Message.class);

					if (message.isOk()) {

						String filenameLoesug = raetsel.getSchluessel() + "_l" + outputformat.getFilenameExtension();
						byte[] imageLoesung = this.raetselFileService.findImageLoesung(raetsel.getSchluessel());

						result.setImageLoesung(imageLoesung);
						result.setUrlLoesung(output2Url(filenameLoesug));
					}

				}

				return result;

			}

			LOGGER.error("Mist: generieren hat nicht geklappt: " + message.getMessage());
			throw new MjaRuntimeException(message.getMessage());

		} catch (Exception e) {

			String msg = "Beim Generieren des Outputs " + outputformat + " zu Raetsel [schluessel="
				+ raetsel.getSchluessel()
				+ ", uuid=" + raetselUuid + "] ist ein Fehler aufgetreten: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new MjaRuntimeException(msg, e);

		}

	}

	String output2Url(final String filename) {

		return "file://" + latexBaseDir + File.separator + filename;
	}

}
