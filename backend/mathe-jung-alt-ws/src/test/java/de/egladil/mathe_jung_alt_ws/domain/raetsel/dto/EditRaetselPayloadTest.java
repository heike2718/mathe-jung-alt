// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.domain.raetsel.Antwortvorschlag;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * EditRaetselPayloadTest
 */
public class EditRaetselPayloadTest {

	@Nested
	class JsonMappingTests {

		@Test
		void serialize() throws JsonProcessingException {

			Antwortvorschlag[] antwortvorschlage = new Antwortvorschlag[2];
			antwortvorschlage[0] = new Antwortvorschlag().withBuchstabe("A").withText("10 m").withKorrekt(false);
			antwortvorschlage[1] = new Antwortvorschlag().withBuchstabe("B").withText("42 m").withKorrekt(true);

			List<Deskriptor> deskriptoren = new ArrayList<>();

			{

				Deskriptor d = new Deskriptor();
				d.id = 2L;
				d.kontext = "RAETSEL";
				d.name = "Minikänguru";
				deskriptoren.add(d);

			}

			{

				Deskriptor d = new Deskriptor();
				d.id = 3L;
				d.kontext = "RAETSEL";
				d.name = "IKID";
				d.admin = true;
				deskriptoren.add(d);
			}

			{

				Deskriptor d = new Deskriptor();
				d.id = 33L;
				d.kontext = "RAETSEL";
				d.name = "A-2";
				d.admin = false;
				deskriptoren.add(d);
			}

			Raetsel raetsel = new Raetsel("neu").withAntwortvorschlaege(antwortvorschlage)
				.withDeskriptoren(deskriptoren).withFrage("Wie viele Meter sind es bis zur Schule?")
				.withKommentar("Minikänguru 2021").withQuelleId("8ef4d9b8-62a6-4643-8674-73ebaec52d98").withSchluessel("02565")
				.withName("Schulweglänge");

			EditRaetselPayload payload = new EditRaetselPayload();
			payload.setIdAendernderUser("df5d4136-8fa6-4fac-b566-470ff7c3c869");
			payload.setLatexHistorisieren(false);
			payload.setRaetsel(raetsel);

			String json = new ObjectMapper().writeValueAsString(payload);

			System.out.println(json);

		}

		@Test
		void deserialize() throws JsonMappingException, JsonProcessingException {

			// Arrange
			String json = "{\"latexHistorisieren\":false,\"idAendernderUser\":\"df5d4136-8fa6-4fac-b566-470ff7c3c869\",\"raetsel\":{\"id\":\"neu\",\"schluessel\":\"02565\",\"name\":\"Schulweglänge\",\"frage\":\"Wie viele Meter sind es bis zur Schule?\",\"loesung\":null,\"kommentar\":\"Minikänguru 2021\",\"quelleId\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"antwortvorschlaege\":[{\"buchstabe\":\"A\",\"text\":\"10 m\",\"korrekt\":false},{\"buchstabe\":\"B\",\"text\":\"42 m\",\"korrekt\":true}],\"deskriptoren\":[{\"id\":2,\"name\":\"Minikänguru\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":3,\"name\":\"IKID\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":33,\"name\":\"A-2\",\"admin\":false,\"kontext\":\"RAETSEL\"}]}}\n";

			// Act
			EditRaetselPayload payload = new ObjectMapper().readValue(json, EditRaetselPayload.class);

			// Assert
			Raetsel raetsel = payload.getRaetsel();

			assertEquals("neu", raetsel.getId());
			assertEquals(2, raetsel.getAntwortvorschlaege().length);
			assertEquals(3, raetsel.getDeskriptoren().size());
		}
	}

}