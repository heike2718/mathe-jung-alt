// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.exceptions.LaTeXCompileException;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.generatoren.RaetselGeneratorService;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Outputformat;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.restclient.LaTeXRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * RaetselGeneratorServiceImpl
 */
@ApplicationScoped
public class RaetselGeneratorServiceImpl implements RaetselGeneratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselGeneratorServiceImpl.class);

	private static List<String> TEMPORARY_FILE_EXTENSIONS = Arrays.asList(new String[] { ".aux", ".log", ".out", ".tex", "" });

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@ConfigProperty(name = "latex.generator.preserve.tempfiles")
	boolean preserveTempFiles;

	@Inject
	AuthenticationContext authCtx;

	@RestClient
	@Inject
	LaTeXRestClient laTeXClient;

	@Inject
	RaetselService raetselService;

	@Inject
	RaetselFileService raetselFileService;

	@Override
	public synchronized Images generatePNGsRaetsel(final String raetselUuid, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font) {

		LOGGER.debug("start generate output");

		Raetsel raetsel = loadRaetsel(raetselUuid);

		if (raetsel.isSchreibgeschuetzt()) {

			String userId = authCtx.getUser().getName();

			LOGGER.warn("user {} nicht berechtigt, PNG fuer Raetsel mit SCHLUESSEL={} zu generieren", userId,
				raetsel.getSchluessel());

			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		raetselFileService.generateFrageLaTeX(raetsel, layoutAntwortvorschlaege, font);

		boolean generateLoesung = StringUtils.isNotBlank(raetsel.getLoesung());

		if (generateLoesung) {

			raetselFileService.generateLoesungLaTeX(raetsel, font);
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

				Images result = new Images().withImageFrage(imageFrage);

				if (responseLoesung != null) {

					message = responseLoesung.readEntity(MessagePayload.class);

					if (message.isOk()) {

						byte[] imageLoesung = this.raetselFileService.findImageLoesung(raetsel.getSchluessel());

						// System.out.println(new String(Base64.getEncoder().encode(imageLoesung)));

						result.withImageLoesung(imageLoesung);
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

				LOGGER.info("Raetsel Images generiert: [raetsel={}, user={}]", raetselUuid,
					StringUtils.abbreviate(authCtx.getUser().getName(), 11));

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
	public synchronized GeneratedFile generatePDFRaetsel(final String raetselUuid, final LayoutAntwortvorschlaege layoutAntwortvorschlaege, final FontName font) {

		LOGGER.debug("start generate output");

		Raetsel raetsel = loadRaetsel(raetselUuid);

		List<String> relevantRoles = PermissionUtils.getRelevantRoles(authCtx);
		boolean hasReadPermission = PermissionUtils.hasReadPermission(relevantRoles,
			raetsel.getStatus());

		if (!hasReadPermission) {

			LOGGER.warn("user {} nicht berechtigt, PDF fuer Raetsel mit SCHLUESSEL={} zu generieren",
				authCtx.getUser().getName(),
				raetsel.getSchluessel());

			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		boolean isOrdinaryUser = PermissionUtils.isUserOrdinary(relevantRoles);

		raetselFileService.generateFrageUndLoesung(raetsel, layoutAntwortvorschlaege, font, isOrdinaryUser);

		Response response = null;
		LOGGER.debug("vor Aufruf LaTeXRestClient");

		try {

			response = laTeXClient.latex2PDF(raetsel.getSchluessel() + RaetselFileService.SUFFIX_PDF);

			LOGGER.debug("nach Aufruf LaTeXRestClient");
			MessagePayload message = response.readEntity(MessagePayload.class);

			String filename = raetsel.getSchluessel() + Outputformat.PDF.getFilenameExtension();

			if (message.isOk()) {

				byte[] pdf = this.raetselFileService.findPDF(raetsel.getSchluessel());

				if (pdf == null) {

					String msg = "Das generierte PDF zu Raetsel [schluessel="
						+ raetsel.getSchluessel()
						+ ", uuid=" + raetselUuid + "] konnte nicht geladen werden. Bitte mal das doc-Verzeichnis prüfen.";
					LOGGER.error(msg);
					throw new MjaRuntimeException(msg);
				}

				GeneratedFile result = new GeneratedFile();
				result.setFileData(pdf);
				result.setFileName(filename);

				raetselFileService
					.deleteTemporaryFiles(new String[] { raetsel.getSchluessel() + RaetselFileService.SUFFIX_PDF + ".tex" });

				LOGGER.info("Raetsel PDF generiert: [raetsel={}, user={}]", raetselUuid,
					StringUtils.abbreviate(authCtx.getUser().getUuid(), 11));

				this.deleteTemporaryFiles(raetsel.getSchluessel());

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

	void deleteTemporaryFiles(final String fileNameWithoutExtension) {

		if (preserveTempFiles) {

			LOGGER.info("tempfiles sollen aufgehoben werden, also fuer {} nicht loeschen", fileNameWithoutExtension);

			return;
		}

		final String path = latexBaseDir + File.separator + fileNameWithoutExtension;

		String[] paths = TEMPORARY_FILE_EXTENSIONS.stream().map(ext -> new String(path + ext)).toList().toArray(new String[0]);

		MjaFileUtils.deleteTemporaryFiles(paths);

	}
}
