// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.validation.MjaRegexps;

/**
 * FindPathsGrafikParser
 */
public class FindPathsGrafikParser {

	private final Pattern pattern;

	/**
	 *
	 */
	public FindPathsGrafikParser() {

		pattern = Pattern.compile(MjaRegexps.REGEXP_RELATIVE_PATH_EPS_IN_TEXT);

	}

	/**
	 * Sucht die relativem Pfade von Grafiken in einem Latex-Text.
	 *
	 * @param  latex
	 *               String darf blank sein
	 * @return       List immer
	 */
	public List<String> findPaths(final String latex) {

		ArrayList<String> result = new ArrayList<>();

		if (StringUtils.isBlank(latex)) {

			return result;
		}

		Matcher matcher = pattern.matcher(latex);

		while (matcher.find()) {

			result.add(matcher.group());
		}

		return result;
	}
}
