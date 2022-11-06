// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

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
	public static void writeOutput(final File file, final String template, final String errorMessage) {

		try (Reader reader = new StringReader(template); FileOutputStream fos = new FileOutputStream(file)) {

			IOUtils.copy(reader, fos, Charset.forName("UTF-8"));
			fos.flush();

		} catch (IOException e) {

			String message = "konnte kein LaTex-File schreiben: [" + errorMessage + "]";
			LOGGER.error(message + ": " + e.getMessage(), e);
			throw new MjaRuntimeException(message);

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

	public static void deleteTemporaryFiles(final String... paths) {

		for (String path : paths) {

			boolean deleted = new File(path).delete();

			if (!deleted) {

				LOGGER.warn("File {} wurde nicht gelöscht", path);
			}
		}
	}

	public static String nameToFilenamePart(final String name) {

		String result = name.toLowerCase().replaceAll(" ", "_");

		result = result.replaceAll("ä", "ae");
		result = result.replaceAll("ö", "oe");
		result = result.replaceAll("ü", "ue");
		result = result.replaceAll("ß", "ss");
		return result;

	}
}
