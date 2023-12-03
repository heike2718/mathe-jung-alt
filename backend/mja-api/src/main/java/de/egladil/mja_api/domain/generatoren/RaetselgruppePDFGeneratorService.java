// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.exceptions.LaTeXCompileException;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.generatoren.impl.QuizitemLaTeXGenerator;
import de.egladil.mja_api.domain.generatoren.impl.RaetselgruppeGeneratorStrategy;
import de.egladil.mja_api.domain.raetsel.Outputformat;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.restclient.LaTeXRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/**
 * RaetselgruppePDFGeneratorService
 */
@ApplicationScoped
public class RaetselgruppePDFGeneratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppePDFGeneratorService.class);

	private static List<String> TEMPORARY_FILE_EXTENSIONS = Arrays.asList(new String[] { ".aux", ".log", ".out", ".tex", "" });

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@ConfigProperty(name = "latex.generator.preserve.tempfiles")
	boolean preserveTempFiles;

	@RestClient
	@Inject
	LaTeXRestClient laTeXClient;

	@Inject
	QuizitemLaTeXGenerator quizitemLaTeXGenerator;

	@Inject
	RaetselService raetselService;

	/**
	 * Generiert LaTeX für die gegebene raetselguppe.
	 *
	 * @param  raetselgruppe
	 *                                  PersistenteRaetselgruppe Berechtigungsprüfung nimmr aufrufender Service vor.
	 * @param  aufgaben
	 *                                  List nur die Aufgaben, die gedruckt werden sollen. Vorauswahl trifft aufrufender Service.
	 * @param  verwendungszweck
	 *                                  Verwendungszweck entscheidet über die Gruppierung der Aufgaben.
	 * @param  font
	 *                                  FontName
	 * @param  schriftgroesse
	 *                                  Schriftgroesse
	 * @param  layoutAntwortvorschlaege
	 *                                  LayoutAntwortvorschlaege wenn NOOP, werden keine Antwortvorschläge gedruckt. So können aus
	 *                                  multiple
	 *                                  choice- Aufgaben auch Arbeitsblätter werden.
	 * @return                          GeneratedFile - ein PDF oder eine LaTeX-Textdatei
	 */
	public GeneratedFile generate(final RaetselgruppeGeneratorInput input) {

		LOGGER.debug("start generate output");

		Verwendungszweck verwendungszweck = input.getVerwendungszweck();

		if (Verwendungszweck.LATEX == verwendungszweck) {

			throw new IllegalArgumentException("diese Methode funktioniert nicht für Verwendungszweck " + verwendungszweck);
		}

		PersistenteRaetselgruppe raetselgruppe = input.getRaetselgruppe();
		RaetselgruppeGeneratorStrategy strategy = RaetselgruppeGeneratorStrategy.getStrategy(verwendungszweck);

		String template = strategy.generateLaTeX(input, raetselService, quizitemLaTeXGenerator);

		String fileNameWithoutExtension = writeToDoc(template, raetselgruppe.uuid, raetselgruppe.name);
		return generatePdf(fileNameWithoutExtension, raetselgruppe.uuid);

	}

	String writeToDoc(final String template, final String raetselgruppeID, final String raetselgruppeName) {

		String filenameWithoutExtension = getFilenameWithoutExcension(raetselgruppeName);
		String fileName = filenameWithoutExtension + ".tex";

		String path = latexBaseDir + File.separator + fileName;
		File file = new File(path);

		String errorMessage = "konnte kein LaTex-File schreiben Raetsel";

		MjaFileUtils.writeTextfile(file, template, errorMessage);

		return filenameWithoutExtension;

	}

	/**
	 * @param  raetselgruppeName
	 * @return
	 */
	String getFilenameWithoutExcension(final String raetselgruppeName) {

		String filenameWithoutExtension = MjaFileUtils.nameToFilenamePart(raetselgruppeName) + "-"
			+ UUID.randomUUID().toString().substring(0, 8);
		return filenameWithoutExtension;
	}

	GeneratedFile generatePdf(final String fileNameWithoutExtension, final String raetselgruppeID) {

		Response response = null;
		LOGGER.debug("vor Aufruf LaTeXRestClient");

		try {

			response = laTeXClient.latex2PDF(fileNameWithoutExtension);

			LOGGER.debug("nach Aufruf LaTeXRestClient");
			MessagePayload message = response.readEntity(MessagePayload.class);

			String filename = fileNameWithoutExtension + Outputformat.PDF.getFilenameExtension();

			String path = latexBaseDir + File.separator + filename;

			if (message.isOk()) {

				byte[] pdf = MjaFileUtils.loadBinaryFile(path, false);

				if (pdf == null) {

					String msg = "Das generierte PDF zur Raetselgruppe [uuid=" + raetselgruppeID
						+ "] konnte nicht geladen werden. Bitte mal das doc-Verzeichnis prüfen.";
					LOGGER.error(msg);
					throw new MjaRuntimeException(msg);
				}

				GeneratedFile result = new GeneratedFile();
				result.setFileData(pdf);
				result.setFileName(filename);

				this.deleteTemporaryFiles(fileNameWithoutExtension);

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

			String msg = "Beim Generieren des Outputs " + Outputformat.PDF + " zu Raetselgruppe [uuid=" + raetselgruppeID
				+ "] ist ein Fehler aufgetreten: " + e.getMessage();
			LOGGER.error(msg, e);
			throw new MjaRuntimeException(msg, e);
		} finally {

			if (response != null) {

				response.close();
			}
		}
	}

	void deleteTemporaryFiles(final String fileNameWithoutExtension) {

		if (preserveTempFiles) {

			LOGGER.debug("tempfiles sollen aufgehoben werden, also fuer {} nicht loeschen", fileNameWithoutExtension);

			return;
		}

		final String path = latexBaseDir + File.separator + fileNameWithoutExtension;

		String[] paths = TEMPORARY_FILE_EXTENSIONS.stream().map(ext -> new String(path + ext)).toList().toArray(new String[0]);

		MjaFileUtils.deleteFiles(paths);

	}
}
