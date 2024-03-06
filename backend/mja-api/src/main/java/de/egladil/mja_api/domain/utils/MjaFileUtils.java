// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;

/**
 * MjaFileUtils
 */
public class MjaFileUtils {

	public static final Logger LOGGER = LoggerFactory.getLogger(MjaFileUtils.class);

	/**
	 * @param  file
	 * @param  template
	 * @return          String
	 */
	public static void writeTextfile(final File file, final String template, final String errormessage) {

		LOGGER.debug("file={}", file.getAbsolutePath());

		try (Reader reader = new StringReader(template); FileOutputStream fos = new FileOutputStream(file)) {

			IOUtils.copy(reader, fos, Charset.forName("UTF-8"));
			fos.flush();

		} catch (IOException e) {

			String message = "konnte kein LaTex-File schreiben: [" + errormessage + "]";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);

		}
	}

	/**
	 * @param file
	 * @param data
	 */
	public static void writeBinaryFile(final File file, final byte[] data) {

		try (FileOutputStream fos = new FileOutputStream(file);
			InputStream in = new ByteArrayInputStream(data)) {

			IOUtils.copy(in, fos);
			fos.flush();
		} catch (IOException e) {

			LOGGER.error("Fehler beim Speichern im Filesystem: " + e.getMessage(), e);
			throw new MjaRuntimeException("Konnte Image nicht ins Filesystem speichern: " + e.getMessage(), e);
		}
	}

	/**
	 * Ein vorhandenes Verzeichnis wird unter Erhaltung der Filehierarchie gezipt.
	 *
	 * @param directoryToZip
	 */
	public static final File createZipArchive(final File directoryToZip) {

		String zipFileName = directoryToZip.getName() + ".zip";
		File zipFile = new File(directoryToZip.getParentFile().getAbsolutePath() + File.separator + zipFileName);

		try (FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zipOut = new ZipOutputStream(fos)) {

			zipFile(directoryToZip, directoryToZip.getName(), zipOut);

			return zipFile;
		} catch (IOException e) {

			String message = "konnte kein Zip-Archiv erzeugen: directoryToZip=" + directoryToZip.getAbsolutePath();
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);
		}
	}

	private static void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut) throws IOException {

		if (fileToZip.isHidden()) {

			return;
		}

		if (fileToZip.isDirectory()) {

			if (fileName.endsWith("/")) {

				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {

				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();

			for (File childFile : children) {

				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}

		try (FileInputStream fis = new FileInputStream(fileToZip)) {

			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;

			while ((length = fis.read(bytes)) >= 0) {

				zipOut.write(bytes, 0, length);
			}
		}
	}

	/**
	 * Legt ein Verzeichnis an.
	 *
	 * @param  path
	 * @param  errorMessage
	 * @return              File
	 */
	public static File createDirectory(final String path, final String errorMessage) throws MjaRuntimeException {

		LOGGER.debug("path={}", path);

		File directory = new File(path);

		try {

			FileUtils.forceMkdir(directory);

			return directory;
		} catch (IOException e) {

			String message = "konnte kein Verzeichnis erzeugen: [" + errorMessage + "]";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);
		}

	}

	/**
	 * Kopiert alle Dateien aus dem sourceDir in das targetDir.
	 *
	 * @param sourceDir
	 * @param targetDir
	 */
	public static void copyFiles(final List<File> files, final File targetDir) {

		if (!targetDir.exists() || !targetDir.isDirectory()) {

			try {

				FileUtils.forceMkdir(targetDir);
			} catch (IOException e) {

				String message = "IOException beim Erzeugen des Verzeichnissubtrees " + targetDir.getAbsolutePath();
				LOGGER.error(message + ": " + e.getMessage(), e);
				throw new MjaRuntimeException(message);
			}
		}

		for (File file : files) {

			try {

				FileUtils.copyFileToDirectory(file, targetDir);
			} catch (IOException e) {

				String message = "IOException beim Kopieren von " + file.getName() + " nach " + targetDir.getAbsolutePath();
				LOGGER.error(message + ": " + e.getMessage(), e);
				throw new MjaRuntimeException(message);

			}
		}
	}

	/**
	 * Verschiebt eine Datei an einen anderen Ort.
	 *
	 * @param source
	 * @param target
	 */
	public static void moveFile(final File source, final File target) {

		if (source.isFile() && source.canRead()) {

			try {

				if (target.exists()) {

					// Eigentlich wollte ich das tun, aber ich habe keinen Bock zu schauen, warum
					// StandardCopyOption.REPLACE_EXISTING ignoriert wird.
					// FileUtils.moveFile(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

					FileUtils.delete(target);
					LOGGER.debug("{} deleted", target.getAbsolutePath());
				}

				FileUtils.moveFile(source, target, StandardCopyOption.COPY_ATTRIBUTES);

				LOGGER.debug("{} verschoben nach {}", source.getAbsolutePath(), target.getAbsolutePath());
			} catch (IOException e) {

				LOGGER.error("Fehler beim Verschieben einer Datei: " + e.getMessage(), e);
				throw new MjaRuntimeException("Datei " + source.getAbsolutePath() + " konnte nicht verschoben werden");
			}
		} else {

			LOGGER.warn("Datei {} existiert nicht oder hat keine write permission.", source.getAbsolutePath());
		}

	}

	/**
	 * @param  classpathLocation
	 *                           String
	 * @return                   String
	 */
	public static String loadTemplate(final String classpathLocation) {

		try (InputStream in = MjaFileUtils.class.getResourceAsStream(classpathLocation);
			StringWriter sw = new StringWriter()) {

			if (in == null) {

				String message = classpathLocation + " existiert nicht!!!!";
				LOGGER.error(message);
				throw new IOException(message);
			}

			IOUtils.copy(in, sw, Charset.forName("UTF-8"));

			return sw.toString();
		} catch (IOException e) {

			String message = "konnte das template nicht laden";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);

		}

	}

	/**
	 * Läd ein binäres File, also png oder pdf oder so.
	 *
	 * @param  path
	 * @param  isTemporaryFile
	 *                         boolean, wenn true, dann wird es anschließend gelöscht.
	 * @return                 byte[] oder null
	 */
	public static byte[] loadBinaryFile(final String path, final boolean isTemporaryFile) {

		LOGGER.debug("path={}", path);

		File file = new File(path);

		if (file.exists() && file.isFile()) {

			try (FileInputStream in = new FileInputStream(file)) {

				return in.readAllBytes();

			} catch (IOException e) {

				LOGGER.warn("konnte {}.png nicht laden - wird ignoriert: {}", path, e.getMessage(), e);
				return null;

			} finally {

				if (isTemporaryFile) {

					file.delete();
				}
			}
		}

		return null;
	}

	public static void deleteFiles(final String... paths) {

		for (String path : paths) {

			File file = new File(path);

			boolean exists = file.exists() && file.canRead();
			boolean deleted = file.delete();

			if (exists && !deleted) {

				LOGGER.warn("File {} wurde nicht geloescht", path);
			}
		}
	}

	public static void deleteDirectoryQuietly(final File file) {

		try {

			FileUtils.deleteDirectory(file);
		} catch (IOException e) {

			LOGGER.warn("Verzeichnis {} wurde nicht geloescht: {}", file.getAbsolutePath(), e.getMessage());

		}
	}

	public static String nameToFilenamePart(final String name) {

		String result = name.toLowerCase().replaceAll(" ", "_");

		result = result.replaceAll("ä", "ae");
		result = result.replaceAll("ö", "oe");
		result = result.replaceAll("ü", "ue");
		result = result.replaceAll("ß", "ss");

		result = result.replaceAll("_-_", "_");
		return result;

	}

	public static String readTextFile(final String filePath) {

		StringBuffer sb = new StringBuffer();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			String line;

			while ((line = reader.readLine()) != null) {

				sb.append(line);
			}
		} catch (IOException e) {

			LOGGER.error(e.getMessage());
			return null;
		}

		return sb.toString();

	}
}
