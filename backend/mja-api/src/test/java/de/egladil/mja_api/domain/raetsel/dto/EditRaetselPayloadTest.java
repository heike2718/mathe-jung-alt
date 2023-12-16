// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_api.domain.raetsel.HerkunftRaetsel;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.test.junit.QuarkusTest;

/**
 * EditRaetselPayloadTest
 */
@QuarkusTest
public class EditRaetselPayloadTest {

	@Test
	void serialize() throws JsonProcessingException {

		Antwortvorschlag[] antwortvorschlage = new Antwortvorschlag[2];
		antwortvorschlage[0] = new Antwortvorschlag().withBuchstabe("A").withText("10 m").withKorrekt(false);
		antwortvorschlage[1] = new Antwortvorschlag().withBuchstabe("B").withText("42 m").withKorrekt(true);

		List<Deskriptor> deskriptoren = new ArrayList<>();

		{

			Deskriptor d = new Deskriptor();
			d.id = 2L;
			d.name = "Minikänguru";
			deskriptoren.add(d);

		}

		{

			Deskriptor d = new Deskriptor();
			d.id = 3L;
			d.name = "IKID";
			d.admin = true;
			deskriptoren.add(d);
		}

		{

			Deskriptor d = new Deskriptor();
			d.id = 33L;
			d.name = "A-2";
			d.admin = false;
			deskriptoren.add(d);
		}

		String quelleId = "8ef4d9b8-62a6-4643-8674-73ebaec52d98";

		QuelleDto quelle = new QuelleDto();
		quelle.setId(quelleId);
		quelle.setPerson("Heike Winkelvoß");
		quelle.setQuellenart(Quellenart.PERSON);

		HerkunftRaetsel herkunft = new HerkunftRaetsel()
			.withHerkunftstyp(RaetselHerkunftTyp.EIGENKREATION)
			.withId(quelleId)
			.withQuellenart(Quellenart.PERSON)
			.withText("Heike Winkelvoß");

		Raetsel raetsel = new Raetsel("neu").withAntwortvorschlaege(antwortvorschlage)
			.withDeskriptoren(deskriptoren).withFrage("Wie viele Meter sind es bis zur Schule?")
			.withKommentar("Minikänguru 2021").withSchluessel("02565")
			.withName("Schulweglänge")
			.withFreigebeben(true);
		raetsel.setHerkunft(herkunft);

		EditRaetselPayload payload = new EditRaetselPayload();
		payload.setLatexHistorisieren(false);
		payload.setRaetsel(raetsel);
		payload.setQuelle(quelle);

		String json = new ObjectMapper().writeValueAsString(payload);

		System.out.println("");
		System.out.println(json);
		System.out.println("");

	}

	@Test
	void deserialize() throws JsonMappingException, JsonProcessingException {

		// Arrange
		String json = "{\"latexHistorisieren\":false,\"raetsel\":{\"id\":\"neu\",\"schluessel\":\"02565\",\"name\":\"Schulweglänge\",\"frage\":\"Wie viele Meter sind es bis zur Schule?\",\"loesung\":null,\"kommentar\":\"Minikänguru 2021\",\"freigegeben\":true,\"herkunft\":{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"herkunftstyp\":\"EIGENKREATION\",\"text\":\"Heike Winkelvoß\",\"mediumUuid\":null},\"schreibgeschuetzt\":true,\"antwortvorschlaege\":[{\"buchstabe\":\"A\",\"text\":\"10 m\",\"korrekt\":false},{\"buchstabe\":\"B\",\"text\":\"42 m\",\"korrekt\":true}],\"deskriptoren\":[{\"id\":2,\"name\":\"Minikänguru\",\"admin\":false},{\"id\":3,\"name\":\"IKID\",\"admin\":true},{\"id\":33,\"name\":\"A-2\",\"admin\":false}],\"embeddableImageInfos\":[],\"images\":null,\"raetselPDF\":null},\"quelle\":{\"id\":\"8ef4d9b8-62a6-4643-8674-73ebaec52d98\",\"quellenart\":\"PERSON\",\"klasse\":null,\"stufe\":null,\"ausgabe\":null,\"jahr\":null,\"seite\":null,\"person\":\"Heike Winkelvoß\",\"pfad\":null,\"mediumUuid\":null}}";

		EditRaetselPayload payload = new ObjectMapper().readValue(json, EditRaetselPayload.class);

		// Assert
		Raetsel raetsel = payload.getRaetsel();

		assertEquals("neu", raetsel.getId());
		assertEquals(2, raetsel.getAntwortvorschlaege().length);
		assertEquals(3, raetsel.getDeskriptoren().size());
		assertTrue(raetsel.isFreigegeben());
	}

}
