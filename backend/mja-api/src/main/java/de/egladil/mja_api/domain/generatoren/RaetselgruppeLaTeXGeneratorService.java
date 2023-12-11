// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.generatoren.dto.RaetselgruppeGeneratorInput;
import de.egladil.mja_api.domain.generatoren.impl.LaTeXDocGeneratorStrategy;
import de.egladil.mja_api.domain.generatoren.impl.LaTeXDocGeneratorType;
import de.egladil.mja_api.domain.generatoren.impl.QuizitemLaTeXGenerator;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.quiz.impl.QuizaufgabeComparator;
import de.egladil.mja_api.domain.raetsel.EmbeddedImagesService;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.utils.MjaFileUtils;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * RaetselgruppeLaTeXGeneratorService generiert für eine Rätselgruppe ein zip-Archiv, das alles LaTeX-Files und eingebetteten
 * Grafiken enthält.
 */
@ApplicationScoped
public class RaetselgruppeLaTeXGeneratorService {

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
	 * @param  input
	 * @return       GeneratedFile
	 */
	public File generateLaTeXArchive(final RaetselgruppeGeneratorInput input) {

		PersistenteAufgabensammlung persistenteRaetselgruppe = input.getRaetselgruppe();

		List<Quizaufgabe> aufgaben = input.getAufgaben();
		Collections.sort(aufgaben, new QuizaufgabeComparator());
		List<String> schluesselliste = aufgaben.stream().map(a -> a.getSchluessel()).toList();

		List<PersistentesRaetsel> trefferliste = raetselDao.findWithSchluesselListe(schluesselliste);
		List<RaetselLaTeXDto> raetselLaTeX = trefferliste.stream().map(pr -> RaetselLaTeXDto.mapFromDB(pr)).toList();

		String dirnameRaetselgruppe = MjaFileUtils.nameToFilenamePart(persistenteRaetselgruppe.name) + "_"
			+ UUID.randomUUID().toString().substring(0, 8);

		// schreiben die generierten Strings zunächst ins Filesystem und zipen dann das Verzeichnis dirnameRaetselgruppe.

		String selfcontainedContent = LaTeXDocGeneratorStrategy.getStrategy(LaTeXDocGeneratorType.SELFCONTAINED).generateLaTeX(
			aufgaben, raetselLaTeX,
			quizitemLaTeXGenerator, input);

		String laTeXContentAufgabenMaster = LaTeXDocGeneratorStrategy.getStrategy(LaTeXDocGeneratorType.MAIN_AUFGABEN)
			.generateLaTeX(aufgaben, raetselLaTeX,
				quizitemLaTeXGenerator, input);

		String laTeXContentLoesungenMaster = LaTeXDocGeneratorStrategy.getStrategy(LaTeXDocGeneratorType.MAIN_LOESUNGEN)
			.generateLaTeX(aufgaben, raetselLaTeX,
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

		File includeDir = new File(pathDirRaetselgruppe + File.separator + "include");
		copyIncludeFilesToTargetDir(includeDir);

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

		checkAndMoveEPS(dirRaetselgruppe);

		File zip = MjaFileUtils.createZipArchive(dirRaetselgruppe);

		MjaFileUtils.deleteDirectoryQuietly(dirRaetselgruppe);

		return zip;
	}

	/**
	 * @param dirRaetselgruppe
	 */
	private void checkAndMoveEPS(final File dirRaetselgruppe) {

		File[] epsFiles = dirRaetselgruppe.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {

				if (name.endsWith(".eps")) {

					return true;
				}

				return false;
			}
		});

		for (File epsFile : epsFiles) {

			String subdirName = "resources" + File.separator + epsFile.getName().substring(0, 1);

			File target = new File(dirRaetselgruppe + File.separator + subdirName + File.separator + epsFile.getName());
			MjaFileUtils.moveFile(epsFile, target);
		}
	}

	void copyIncludeFilesToTargetDir(final File targetDirectory) {

		File includeDir = new File(latexBaseDir + File.separator + "include");

		File[] children = includeDir.listFiles();

		MjaFileUtils.copyFiles(Arrays.asList(children), targetDirectory);
	}

}
