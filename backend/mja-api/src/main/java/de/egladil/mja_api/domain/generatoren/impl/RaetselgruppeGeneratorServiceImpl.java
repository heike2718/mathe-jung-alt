// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.exceptions.LaTeXCompileException;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.RaetselgruppeGeneratorService;
import de.egladil.mja_api.domain.generatoren.dto.RaetselGeneratorinput;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Outputformat;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedPDF;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.restclient.LaTeXRestClient;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * RaetselgruppeGeneratorServiceImpl
 */
@ApplicationScoped
public class RaetselgruppeGeneratorServiceImpl implements RaetselgruppeGeneratorService {

	private static final String LATEX_TEMPLATE = "/latex/template-quiz-vorschau.tex";

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppeGeneratorServiceImpl.class);

	private static List<String> TEMPORARY_FILE_EXTENSIONS = Arrays.asList(new String[] { ".aux", ".log", ".out", ".tex", "" });

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@RestClient
	@Inject
	LaTeXRestClient laTeXClient;

	@Inject
	QuizitemLaTeXGenerator quizitemLaTeXGenerator;

	@Inject
	RaetselService raetselService;

	@Override
	public GeneratedPDF generatePDFQuiz(final PersistenteRaetselgruppe raetselgruppe, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		return null;
	}

	@Override
	public GeneratedPDF generateVorschauPDFQuiz(final PersistenteRaetselgruppe raetselgruppe, final List<Quizaufgabe> aufgaben, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		LOGGER.debug("start generate output");

		String template = MjaFileUtils.loadTemplate(LATEX_TEMPLATE);

		template = template.replace(LaTeXPlaceholder.UEBERSCHRIFT.placeholder(), raetselgruppe.name);

		List<String> schluessel = aufgaben.stream().map(a -> a.getSchluessel()).toList();
		List<RaetselLaTeXDto> raetselLaTeX = raetselService.findRaetselLaTeXwithSchluessel(schluessel);

		StringBuffer sb = new StringBuffer();

		int count = 0;

		for (Quizaufgabe aufgabe : aufgaben) {

			Optional<RaetselLaTeXDto> opt = raetselLaTeX.stream().filter(r -> aufgabe.getSchluessel().equals(r.getSchluessel()))
				.findFirst();

			if (opt.isPresent()) {

				RaetselLaTeXDto raetsel = opt.get();

				RaetselGeneratorinput input = new RaetselGeneratorinput().withAntwortvorschlaege(aufgabe.getAntwortvorschlaege())
					.withFrage(raetsel.getFrage()).withLoesung(raetsel.getLoesung())
					.withLayoutAntwortvorschlaege(layoutAntwortvorschlaege);

				String aufgabeSchluessel = LaTeXConstants.HEADER_AUFGABE.replace("{0}", aufgabe.getSchluessel());

				sb.append(aufgabeSchluessel);
				sb.append("\n");
				String textFrageLoesung = quizitemLaTeXGenerator.generateLaTeXFrageLoesung(input);
				sb.append(textFrageLoesung);

				if (count < aufgaben.size() - 1) {

					template += LaTeXConstants.VALUE_PAR;
				}
				count++;

			} else {

				LOGGER.warn("Zu schuessel {} wurde kein RAETSEL in der DB gefunden");
			}
		}

		template = template.replace(LaTeXPlaceholder.CONTENT.placeholder(), sb.toString());

		String fileNameWithoutExtension = writeToDoc(template, raetselgruppe.uuid, raetselgruppe.name);

		return generatePdf(fileNameWithoutExtension, raetselgruppe.uuid);
	}

	String writeToDoc(final String template, final String raetselgruppeID, final String raetselgruppeName) {

		String filenameWithoutExtension = MjaFileUtils.nameToFilenamePart(raetselgruppeName) + "-"
			+ UUID.randomUUID().toString().substring(0, 8);
		String fileName = filenameWithoutExtension + ".tex";

		String path = latexBaseDir + File.separator + fileName;
		File file = new File(path);

		String errorMessage = "konnte kein LaTex-File schreiben Raetsel";

		MjaFileUtils.writeOutput(file, template, errorMessage);

		return filenameWithoutExtension;

	}

	GeneratedPDF generatePdf(final String fileNameWithoutExtension, final String raetselgruppeID) {

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

				String url = MjaFileUtils.output2Url(path);

				if (pdf == null) {

					String msg = "Das generierte PDF zu Raetsel [uuid=" + raetselgruppeID + "] konnte nicht geladen werden. Bitte "
						+ url + " prüfen";
					LOGGER.error(msg);
					throw new MjaRuntimeException(msg);
				}

				GeneratedPDF result = new GeneratedPDF();
				result.setFileData(pdf);
				result.setUrl(url);
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

		final String path = latexBaseDir + File.separator + fileNameWithoutExtension;

		String[] paths = TEMPORARY_FILE_EXTENSIONS.stream().map(ext -> new String(path + ext)).toList().toArray(new String[0]);

		MjaFileUtils.deleteTemporaryFiles(paths);

	}
}
