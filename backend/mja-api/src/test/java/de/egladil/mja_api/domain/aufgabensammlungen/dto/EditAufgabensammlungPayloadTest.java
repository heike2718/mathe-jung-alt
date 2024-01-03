// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import io.quarkus.test.junit.QuarkusTest;

/**
 * EditAufgabensammlungPayloadTest
 */
@QuarkusTest
public class EditAufgabensammlungPayloadTest {

	@Nested
	class JsonMappingTests {

		@Test
		void testSerialize() throws JsonProcessingException {

			// Arrange
			EditAufgabensammlungPayload payload = new EditAufgabensammlungPayload();
			payload.setId("neu");
			payload.setName("Minikänguruwettbewerb 2022 - Klasse 1");
			payload.setReferenz("2022");
			payload.setReferenztyp(Referenztyp.MINIKAENGURU);
			payload.setSchwierigkeitsgrad(Schwierigkeitsgrad.IKID);
			payload.setFreigegeben(false);
			payload.setPrivat(false);

			// Act
			String json = new ObjectMapper().writeValueAsString(payload);

			System.out.println(json);
		}
	}

}
