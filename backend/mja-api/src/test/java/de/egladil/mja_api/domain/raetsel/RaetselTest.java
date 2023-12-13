// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.test.junit.QuarkusTest;

/**
 * RaetselTest
 */
@QuarkusTest
public class RaetselTest {

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

		String quelleId = "8ef4d9b8-62a6-4643-8674-73ebaec52d98";

		HerkunftRaetsel quelle = new HerkunftRaetsel().withId(quelleId).withText("Ponder")
			.withId(quelleId).withQuellenart(Quellenart.PERSON)
			.withHerkunftstyp(RaetselHerkunftTyp.EIGENKREATION);

		Raetsel raetsel = new Raetsel("df5d4136-8fa6-4fac-b566-470ff7c3c869").withAntwortvorschlaege(antwortvorschlage)
			.withDeskriptoren(deskriptoren).withFrage("Wie viele Meter sind es bis zur Schule?")
			.withKommentar("Minikänguru 2021").withSchluessel("02565")
			.withName("Schulweglänge").withFilenameVorschauFrage("2b9afae0-58b9.png")
			.withFilenameVorschauLoesung("8d82467c-4d56.png");
		raetsel.setHerkunft(quelle);

		String json = new ObjectMapper().writeValueAsString(raetsel);

		System.out.println(json);

	}

	@Test
	void deserialize() throws JsonMappingException, JsonProcessingException {

		// Arrange
		String json = "{\"id\":\"df5d4136-8fa6-4fac-b566-470ff7c3c869\",\"schluessel\":\"02565\",\"name\":\"Schulweglänge\",\"frage\":\"Wie viele Meter sind es bis zur Schule?\",\"loesung\":null,\"kommentar\":\"Minikänguru 2021\",\"freigegeben\":false,\"herkunft\":{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"herkunftstyp\":\"EIGENKREATION\",\"text\":\"Ponder\",\"mediumUuid\":null},\"schreibgeschuetzt\":true,\"antwortvorschlaege\":[{\"buchstabe\":\"A\",\"text\":\"10 m\",\"korrekt\":false},{\"buchstabe\":\"B\",\"text\":\"42 m\",\"korrekt\":true}],\"deskriptoren\":[{\"id\":2,\"name\":\"Minikänguru\",\"admin\":false,\"kontext\":\"RAETSEL\"},{\"id\":3,\"name\":\"IKID\",\"admin\":true,\"kontext\":\"RAETSEL\"},{\"id\":33,\"name\":\"A-2\",\"admin\":false,\"kontext\":\"RAETSEL\"}],\"embeddableImageInfos\":[],\"images\":null,\"raetselPDF\":null}";

		// Act
		Raetsel raetsel = new ObjectMapper().readValue(json, Raetsel.class);

		// Assert
		assertEquals("df5d4136-8fa6-4fac-b566-470ff7c3c869", raetsel.getId());
		assertEquals(2, raetsel.getAntwortvorschlaege().length);
		assertEquals(3, raetsel.getDeskriptoren().size());
	}
}
