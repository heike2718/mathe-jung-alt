// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.grafiken.impl;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * FilenameDelegate
 */
@ApplicationScoped
public class FilenameDelegate {

	private static final String FIRST_SUBDIR = "/resources";

	private final Pattern patternNameEpsHeike;

	/**
	 *
	 */
	private FilenameDelegate() {

		patternNameEpsHeike = Pattern.compile(MjaRegexps.REGEXP_EPS_HEIKE_WITH_FILE_SEPERATOR);

	}

	public String getRelativePathForEmbeddableImage(final String uuid, final String filenameImage) {

		String subdir = uuid.substring(0, 1);

		if (matchesEpsNameHeike(filenameImage)) {

			return FIRST_SUBDIR + File.separator + subdir + File.separator + uuid.substring(0, 9) + filenameImage;

		}

		return FIRST_SUBDIR + File.separator + subdir + File.separator + uuid.substring(0, 13) + ".eps";
	}

	public boolean matchesEpsNameHeike(final String filename) {

		Matcher matcher = patternNameEpsHeike.matcher(File.separator + filename);
		return matcher.matches();
	}

}
