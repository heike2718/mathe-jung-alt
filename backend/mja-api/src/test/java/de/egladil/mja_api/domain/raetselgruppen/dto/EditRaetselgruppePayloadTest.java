// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;

/**
 * EditRaetselgruppePayloadTest
 */
public class EditRaetselgruppePayloadTest {

	@Nested
	class JsonMappingTests {

		@Test
		void testSerialize() throws JsonProcessingException {

			// Arrange
			EditRaetselgruppePayload payload = new EditRaetselgruppePayload();
			payload.setId("abcdef-123456");
			payload.setKommentar("ein sinnloser Kommentar");
			payload.setName("Minikänguruwettbewerb 2022 - Klasse 1");
			payload.setReferenz("2022");
			payload.setReferenztyp(Referenztyp.MINIKAENGURU);
			payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.EINS);
			payload.setStatus(DomainEntityStatus.ERFASST);

			// Act
			String json = new ObjectMapper().writeValueAsString(payload);

			System.out.println(json);
		}
	}

}
