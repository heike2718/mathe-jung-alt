// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.generatoren.impl.LaTeXMasterAufgabenGenerator;
import de.egladil.mja_api.domain.generatoren.impl.LaTeXMasterLoesungenGenerator;
import de.egladil.mja_api.domain.generatoren.impl.QuizitemLaTeXGenerator;
import de.egladil.mja_api.domain.generatoren.impl.SelfcontainedLaTeXAufgabenLoesungenGenerator;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.quiz.impl.QuizaufgabeComparator;
import de.egladil.mja_api.domain.raetsel.EmbeddedImagesService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * RaetselgruppeLaTeXGeneratorService generiert für eine Rätselgruppe ein zip-Archiv, das alles LaTeX-Files und eingebetteten
 * Grafiken enthält.
 */
@ApplicationScoped
public class RaetselgruppeLaTeXGeneratorService {

	private static List<String> NAMES_INCLUDE_FILES = Arrays.asList(new String[] { "minimal.tex", "tikz.tex", "layout.tex" });

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppeLaTeXGeneratorService.class);

	@ConfigProperty(name = "latex.base.dir")
	String latexBaseDir;

	@ConfigProperty(name = "latex.generator.preserve.tempfiles")
	boolean preserveTempFiles;

	@Inject
	RaetselDao raetselDao;

	@Inject
	QuizitemLaTeXGenerator quizitemLaTeXGenerator;

	@Inject
	EmbeddedImagesService embeddedImagesService;

	/**
	 * Generiert ein LaTeX-Archiv, also eine zip-Datei, die alle Files enthält, die zum lokalen Compilieren der Rätselgruppe
	 * erforderlich sind.
	 *
	 * @param  raetselgruppe
	 * @param  input
	 * @return               GeneratedFile
	 */
	public GeneratedFile generateLaTeXArchive(final PersistenteRaetselgruppe raetselgruppe, final RaetselgruppeGeneratorInput input) {

		List<Quizaufgabe> aufgaben = input.getAufgaben();
		Collections.sort(aufgaben, new QuizaufgabeComparator());
		List<String> schluesselliste = aufgaben.stream().map(a -> a.getSchluessel()).toList();

		List<PersistentesRaetsel> trefferliste = raetselDao.findWithSchluesselListe(schluesselliste);
		List<RaetselLaTeXDto> raetselLaTeX = trefferliste.stream().map(pr -> RaetselLaTeXDto.mapFromDB(pr)).toList();

		String dirnameRaetselgruppe = MjaFileUtils.nameToFilenamePart(raetselgruppe.name) + "_"
			+ UUID.randomUUID().toString().substring(0, 8);

		// schreiben die generierten Strings zunächst ins Filesystem und zipen dann das Verzeichnis dirnameRaetselgruppe.

		String selfcontainedContent = new SelfcontainedLaTeXAufgabenLoesungenGenerator().generateLaTeX(aufgaben, raetselLaTeX,
			quizitemLaTeXGenerator, input);

		String laTeXContentAufgabenMaster = new LaTeXMasterAufgabenGenerator().generateLaTeX(aufgaben, raetselLaTeX,
			quizitemLaTeXGenerator, input);

		String laTeXContentLoesungenMaster = new LaTeXMasterLoesungenGenerator().generateLaTeX(aufgaben, raetselLaTeX,
			quizitemLaTeXGenerator, input);

		String pathDirRaetselgruppe = latexBaseDir + File.separator + dirnameRaetselgruppe;

		File dirRaetselgruppe = MjaFileUtils.createDirectory(pathDirRaetselgruppe,
			"Fehler beim Erzeugen eines Verzeichnisses für die Raetselgruppe");

		LOGGER.info("Verzeichnis {} angelegt", pathDirRaetselgruppe);

		{

			File file = new File(pathDirRaetselgruppe + File.separator + dirnameRaetselgruppe + "_selfcontained.tex");
			MjaFileUtils.writeTextfile(file, selfcontainedContent, "Fehler beim Schreiben des selfcontained LaTeX");

			LOGGER.info("File {} gespeichert", file.getAbsolutePath());

		}

		{

			File file = new File(pathDirRaetselgruppe + File.separator + dirnameRaetselgruppe + "_aufgaben.tex");
			MjaFileUtils.writeTextfile(file, laTeXContentAufgabenMaster, "Fehler beim Schreiben des laTeXContentAufgabenMaster");

			LOGGER.info("File {} gespeichert", file.getAbsolutePath());
		}

		{

			File file = new File(pathDirRaetselgruppe + File.separator + dirnameRaetselgruppe + "_loesungen.tex");
			MjaFileUtils.writeTextfile(file, laTeXContentLoesungenMaster, "Fehler beim Schreiben des laTeXContentLoesungenMaster");

			LOGGER.info("File {} gespeichert", file.getAbsolutePath());
		}

		copyIncludeFilesToTargetDir(dirRaetselgruppe);

		for (PersistentesRaetsel raetselDB : trefferliste) {

			String pathAufgabe = pathDirRaetselgruppe + File.separator + raetselDB.schluessel + ".tex";
			MjaFileUtils.writeTextfile(new File(pathAufgabe), raetselDB.frage,
				"Fehler beim Schreiben der Frage von " + raetselDB.schluessel);
			LOGGER.info("File {} fertig", pathAufgabe);

			if (StringUtils.isNotBlank(raetselDB.loesung)) {

				String pathLoesung = pathDirRaetselgruppe + File.separator + raetselDB.schluessel + "_l.tex";
				MjaFileUtils.writeTextfile(new File(pathLoesung), raetselDB.loesung,
					"Fehler beim Schreiben der Frage von " + raetselDB.schluessel);
				LOGGER.info("File {} fertig", pathLoesung);
			}

			List<GeneratedFile> embeddedImages = embeddedImagesService.getEmbeddedImages(raetselDB.uuid);

			for (GeneratedFile generatedFile : embeddedImages) {

				String pathImage = pathDirRaetselgruppe + File.separator + generatedFile.getFileName();
				MjaFileUtils.writeBinaryFile(new File(pathImage), generatedFile.getFileData());
				LOGGER.info("File {} fertig", pathImage);

			}
		}

		MjaFileUtils.createZipArchive(dirRaetselgruppe, "Fehler beim Komprimieren");

		String filename = dirnameRaetselgruppe + ".zip";

		byte[] bytes = MjaFileUtils.loadBinaryFile(filename, preserveTempFiles);

		GeneratedFile result = new GeneratedFile();
		result.setFileData(bytes);
		result.setFileName(filename);
		return result;
	}

	void copyIncludeFilesToTargetDir(final File targetDirectory) {

		File includeDir = new File(latexBaseDir + File.separator + "include");

		File[] children = includeDir.listFiles();

		List<File> files = Arrays.stream(children).filter(f -> NAMES_INCLUDE_FILES.contains(f.getName()))
			.collect(Collectors.toList());

		MjaFileUtils.copyFiles(files, targetDirectory);
	}

}
