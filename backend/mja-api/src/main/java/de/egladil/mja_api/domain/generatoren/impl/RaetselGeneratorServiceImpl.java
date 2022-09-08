// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.exceptions.LaTeXCompileException;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.generatoren.RaetselGeneratorService;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Outputformat;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedImages;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedPDF;
import de.egladil.mja_api.infrastructure.restclient.LaTeXRestClient;
import de.egladil.web.mja_auth.dto.MessagePayload;

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
	public synchronized GeneratedImages generatePNGsRaetsel(final String raetselUuid, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		LOGGER.debug("start generate output");

		Raetsel raetsel = loadRaetsel(raetselUuid);

		raetselFileService.generateFrageLaTeX(raetsel, layoutAntwortvorschlaege);

		boolean generateLoesung = StringUtils.isNotBlank(raetsel.getLoesung());

		if (generateLoesung) {

			raetselFileService.generateLoesungLaTeX(raetsel);
		}

		Response responseFrage = null;
		Response responseLoesung = null;
		LOGGER.debug("vor Aufruf LaTeXRestClient");

		try {

			responseFrage = laTeXClient.latex2PNG(raetsel.getSchluessel());

			if (generateLoesung) {

				responseLoesung = laTeXClient.latex2PNG(raetsel.getSchluessel() + RaetselFileService.SUFFIX_LOESUNGEN);
			}

			LOGGER.debug("nach Aufruf LaTeXRestClient");
			MessagePayload message = responseFrage.readEntity(MessagePayload.class);

			String filename = raetsel.getSchluessel() + Outputformat.PNG.getFilenameExtension();
			String filenameLoesung = raetsel.getSchluessel() + RaetselFileService.SUFFIX_LOESUNGEN
				+ Outputformat.PNG.getFilenameExtension();

			if (message.isOk()) {

				byte[] imageFrage = this.raetselFileService.findImageFrage(raetsel.getSchluessel());

				GeneratedImages result = new GeneratedImages();
				result.setImageFrage(imageFrage);
				result.setUrlFrage(output2Url(filename));

				if (responseLoesung != null) {

					message = responseLoesung.readEntity(MessagePayload.class);

					if (message.isOk()) {

						byte[] imageLoesung = this.raetselFileService.findImageLoesung(raetsel.getSchluessel());

						// System.out.println(new String(Base64.getEncoder().encode(imageLoesung)));

						result.setImageLoesung(imageLoesung);
						result.setUrlLoesung(output2Url(filenameLoesung));
					} else {

						LOGGER.error("Mist: generieren der Lösung hat nicht geklappt: " + message.getMessage());
						throw new LaTeXCompileException(
							"Beim Generieren der Lösung ist etwas schiefgegangen: " + message.getMessage())
								.withNameFile(filenameLoesung);
					}

				}

				raetselFileService
					.deleteTemporaryFiles(new String[] { raetsel.getSchluessel() + ".tex" });

				if (generateLoesung) {

					raetselFileService
						.deleteTemporaryFiles(
							new String[] { raetsel.getSchluessel() + RaetselFileService.SUFFIX_LOESUNGEN + ".tex" });
				}

				return result;

			}

			LOGGER.error("Mist: generieren der Frage hat nicht geklappt: " + message.getMessage());
			throw new LaTeXCompileException("Beim Generieren der Frage ist etwas schiefgegangen: " + message.getMessage())
				.withNameFile(filename);

		} catch (LaTeXCompileException e) {

			throw e;
		} catch (Exception e) {

			String msg = "Beim Generieren des Outputs " + Outputformat.PNG + " zu Raetsel [schluessel="
				+ raetsel.getSchluessel()
				+ ", uuid=" + raetselUuid + "] ist ein Fehler aufgetreten: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new MjaRuntimeException(msg, e);

		} finally {

			if (responseFrage != null) {

				responseFrage.close();
			}

			if (responseLoesung != null) {

				responseLoesung.close();
			}
		}

	}

	@Override
	public synchronized GeneratedPDF generatePDFRaetsel(final String raetselUuid, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		LOGGER.debug("start generate output");

		Raetsel raetsel = loadRaetsel(raetselUuid);

		raetselFileService.generateFrageUndLoesung(raetsel, layoutAntwortvorschlaege);

		Response response = null;
		LOGGER.debug("vor Aufruf LaTeXRestClient");

		try {

			response = laTeXClient.latex2PDF(raetsel.getSchluessel() + RaetselFileService.SUFFIX_PDF);

			LOGGER.debug("nach Aufruf LaTeXRestClient");
			MessagePayload message = response.readEntity(MessagePayload.class);

			String filename = raetsel.getSchluessel() + Outputformat.PDF.getFilenameExtension();

			if (message.isOk()) {

				byte[] pdf = this.raetselFileService.findPDF(raetsel.getSchluessel());

				String url = output2Url(filename);

				if (pdf == null) {

					String msg = "Das generierte PDF zu Raetsel [schluessel="
						+ raetsel.getSchluessel()
						+ ", uuid=" + raetselUuid + "] konnte nicht geladen werden. Bitte " + url + " prüfen";
					LOGGER.error(msg);
					throw new MjaRuntimeException(msg);
				}

				GeneratedPDF result = new GeneratedPDF();
				result.setFileData(pdf);
				result.setUrl(url);
				result.setFileName(filename);

				raetselFileService
					.deleteTemporaryFiles(new String[] { raetsel.getSchluessel() + RaetselFileService.SUFFIX_PDF + ".tex" });

				return result;
			}

			LOGGER.error("Mist: generieren des PDFs hat nicht geklappt: " + message.getMessage());
			throw new LaTeXCompileException("Beim Generieren des PDFs ist etwas schiefgegangen: " + message.getMessage())
				.withNameFile(filename);

		} catch (LaTeXCompileException e) {

			throw e;
		} catch (MjaRuntimeException e) {

			throw e;
		} catch (Exception e) {

			String msg = "Beim Generieren des Outputs " + Outputformat.PDF + " zu Raetsel [schluessel="
				+ raetsel.getSchluessel()
				+ ", uuid=" + raetselUuid + "] ist ein Fehler aufgetreten: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new MjaRuntimeException(msg, e);
		} finally {

			if (response != null) {

				response.close();
			}
		}
	}

	/**
	 * @param  raetselUuid
	 * @return                         Raetsel
	 * @throws WebApplicationException
	 *                                 wenn es keinen Eintrag mit der URI gibt oder noch nicht alle erforderlichen Grafikdateien
	 *                                 vorhanden sind.
	 */
	Raetsel loadRaetsel(final String raetselUuid) throws WebApplicationException {

		Raetsel raetsel = raetselService.getRaetselZuId(raetselUuid);

		if (raetsel == null) {

			throw new WebApplicationException(
				Response.status(404).entity(MessagePayload.error("Es gibt kein Raetsel mit dieser UUID")).build());
		}

		List<String> fehlendeGrafiken = raetsel.getGrafikInfos().stream().filter(gi -> !gi.isExistiert()).map(gi -> gi.getPfad())
			.collect(Collectors.toList());

		if (!fehlendeGrafiken.isEmpty()) {

			String message = "Es fehlen noch Grafiken: ";

			for (int i = 0; i < fehlendeGrafiken.size(); i++) {

				message += fehlendeGrafiken.get(i);

				if (i < fehlendeGrafiken.size() - 2) {

					message += ", ";
				}
			}

			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(MessagePayload.error(message)).build());
		}
		return raetsel;
	}

	String output2Url(final String filename) {

		return "file://" + latexBaseDir + File.separator + filename;
	}

}
