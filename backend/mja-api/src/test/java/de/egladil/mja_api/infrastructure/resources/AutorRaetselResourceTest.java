// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.raetsel.HerkunftRaetsel;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.profiles.FullDatabaseAutorTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * AutorRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseAutorTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class AutorRaetselResourceTest {

	private static final String CSRF_TOKEN = "lqhidhqio";

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(1)
	void should_raetselAendernReturn403_when_notTheOwner() throws Exception {

		try (InputStream in = getClass().getResourceAsStream("/payloads/EditRaetselPayloadUpdate.json");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, StandardCharsets.UTF_8);

			String requestBody = sw.toString();

			given()
				.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
				.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.put("v1")
				.then()
				.statusCode(403);
		}
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(2)
	void should_raetselAnlegenMitQuelleZeitschrift_work() {

		// Arrange
		String mediumUuid = "9ab888be-e84b-4c81-ab4d-4451a5097892";

		QuelleDto quelle = new QuelleDto();
		quelle.setMediumUuid(mediumUuid);
		quelle.setPfad("/media/veracrypt2/mathe/zeitschriften/lvz/lvz1.pdf");
		quelle.setSeite("32");
		quelle.setJahr("1965");
		quelle.setQuellenart(Quellenart.ZEITSCHRIFT);
		quelle.setId("neu");

		List<Deskriptor> deskriptoren = new ArrayList<>();

		{

			Deskriptor deskriptor = new Deskriptor("Klassen 3/4", true);
			deskriptor.id = 14l;
			deskriptoren.add(deskriptor);
		}

		{

			Deskriptor deskriptor = new Deskriptor("Mathematik", false);
			deskriptor.id = 1l;
			deskriptoren.add(deskriptor);
		}

		Raetsel raetsel = new Raetsel("neu")
			.withDeskriptoren(deskriptoren)
			.withFrage(
				"Subtrahiere von der kleinsten Zahl mit 4 verschiedenen Ziffern die größte Zahl mit 2 verschiedenen Ziffern.")
			.withKommentar("Ziffern versus Zahlen")
			.withLoesung("$1234 - 98 = 1136")
			.withName("Subtraktion und Ziffernverständnis");
		raetsel.setHerkunft(new HerkunftRaetsel().withHerkunftstyp(RaetselHerkunftTyp.ADAPTATION));

		EditRaetselPayload editRaetselPayload = new EditRaetselPayload();
		editRaetselPayload.setQuelle(quelle);
		editRaetselPayload.setRaetsel(raetsel);

		// Act
		Raetsel result = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(editRaetselPayload)
			.post("v1")
			.then()
			.statusCode(201)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(Raetsel.class);

		assertNotNull(raetsel.getId());

		HerkunftRaetsel quelleUI = result.getHerkunft();
		assertEquals("Frodo Beutlin aus Beutelsend (basierend auf einer Idee aus Leipziger Volkszeitung 1965, S.32)",
			quelleUI.getText());
		assertEquals(Quellenart.ZEITSCHRIFT, quelleUI.getQuellenart());
		assertEquals(mediumUuid, quelleUI.getMediumUuid());
		System.out.println("quelleID=" + quelleUI.getId());
		assertFalse("neu".equals(quelleUI.getId()));
	}
}
