// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.raetsel.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * FindPathsGrafikParserTest
 */
public class FindPathsGrafikParserTest {

	@Test
	void should_returnEmptyGrafikPfade_when_latexNull() throws Exception {

		// Act
		List<String> result = new FindPathsGrafikParser().findPaths(null);

		// Assert
		assertTrue(result.isEmpty());

	}

	@Test
	void should_returnEmptyGrafikPfade_when_latexBlank() throws Exception {

		// Arrange
		String latex = "  ";

		// Act
		List<String> result = new FindPathsGrafikParser().findPaths(latex);

		// Assert
		assertTrue(result.isEmpty());

	}

	@Test
	void should_returnEmptyGrafikPfade_when_keineGrafik() throws Exception {

		// Arrange
		String latex = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-2.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latex = sw.toString();

		}

		// Act
		List<String> result = new FindPathsGrafikParser().findPaths(latex);

		// Assert
		assertTrue(result.isEmpty());

	}

	@Test
	void should_returnGrafikPfadeSize1_when_eineGrafik() throws Exception {

		// Arrange
		String latex = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-1.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latex = sw.toString();

		}

		// Act
		List<String> result = new FindPathsGrafikParser().findPaths(latex);

		// Assert
		assertEquals(1, result.size());
		assertEquals("/resources/002/00963.eps", result.get(0));

	}

	@Test
	void should_returnGrafikPfadeSize2_when_zweiGrafiken() throws Exception {

		// Arrange
		String latex = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-3.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latex = sw.toString();

		}

		// Act
		List<String> result = new FindPathsGrafikParser().findPaths(latex);

		// Assert
		assertEquals(2, result.size());
		assertEquals("/resources/003/01036_0.eps", result.get(0));
		assertEquals("/resources/003/01036_6.eps", result.get(1));

	}

}
