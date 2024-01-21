// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.generatoren.impl.LaTeXConstants;

/**
 * VorschauUtils
 */
public class VorschauUtils {

	/**
	 * Gibt einen sinnvollen Vorschautext zurück.
	 *
	 * @param  text
	 * @param  length
	 * @return
	 */
	public static String getVorschautext(final String text, final int length) {

		String trimmedText = text.trim();

		if (trimmedText.startsWith(LaTeXConstants.CENTER_START)) {

			String theTextAfterCenterStart = trimmedText.substring(LaTeXConstants.CENTER_START.length()).trim();

			if (theTextAfterCenterStart.startsWith(LaTeXConstants.TIKZ_START)
				|| theTextAfterCenterStart.startsWith(LaTeXConstants.INCLUDEGRAPHICS)) {

				String theText = getTheTextAfterLaTeXEnv(theTextAfterCenterStart, LaTeXConstants.CENTER_END);
				return StringUtils.abbreviate(theText, length);

			}
		}

		if (trimmedText.startsWith(LaTeXConstants.TIKZ_START)) {

			String theText = getTheTextAfterLaTeXEnv(trimmedText, LaTeXConstants.TIKZ_END);
			return StringUtils.abbreviate(theText, length);

		}

		return StringUtils.abbreviate(text, length);
	}

	private static String getTheTextAfterLaTeXEnv(final String text, final String latexEnv) {

		int whereTIKZEndStarts = text.indexOf(latexEnv);
		int startIndext = whereTIKZEndStarts + latexEnv.length();

		String theText = text.substring(startIndext).trim();
		return theText;
	}

}
