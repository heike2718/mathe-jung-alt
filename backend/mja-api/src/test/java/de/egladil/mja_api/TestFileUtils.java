// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api;

import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;

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

	public static EditRaetselPayload loadEditRaetselPayload(final String classpath) throws Exception {

		try (InputStream in = TestFileUtils.class.getResourceAsStream(classpath)) {

			EditRaetselPayload result = new ObjectMapper().readValue(in, EditRaetselPayload.class);
			return result;
		}

	}
}
