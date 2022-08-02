// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.raetsel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * FindPathsGrafikParser
 */
public class FindPathsGrafikParser {

	public static final String REGEXP_GRAFIK = "/resources/\\d{3}/\\d{5}[_-]{0,1}\\d{0,2}.eps";

	private final Pattern pattern;

	/**
	 *
	 */
	public FindPathsGrafikParser() {

		pattern = Pattern.compile(REGEXP_GRAFIK);

	}

	/**
	 * Sucht die relativem Pfade von Grafiken in einem Latex-Text.
	 *
	 * @param  latex
	 *               String
	 * @return       List
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
