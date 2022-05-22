// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain;

import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;

/**
 * TestFileUtils
 */
public class TestFileUtils {

	public static Raetsel loadReaetsel() throws Exception {

		try (InputStream in = TestFileUtils.class.getResourceAsStream("/payloads/Raetsel.json")) {

			Raetsel raetsel = new ObjectMapper().readValue(in, Raetsel.class);
			return raetsel;
		}

	}
}
