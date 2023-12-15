// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.medien.Mediensuchmodus;
import de.egladil.mja_api.domain.medien.dto.MediensucheTreffer;
import de.egladil.mja_api.domain.medien.dto.MediensucheTrefferItem;
import de.egladil.mja_api.domain.medien.dto.MediumDto;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * MedienResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(MedienResource.class)
@TestProfile(FullDatabaseTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class MedienResourceTest {

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(1)
	void should_mediumAnlegenAndFindenWork_when_Zeitschrift() {

		// Arrange
		MediumDto mediumDto = new MediumDto()
			.withTitel("Leipziger Volkszeitung")
			.withId("neu").withKommentar("Knobelaufgaben")
			.withMedienart(Medienart.ZEITSCHRIFT)
			.withUrl("https://mathematikalpha.de/")
			.withPfad("/media/veracrypt2/mathe/zeitschriften/lvz/lvz1.pdf");

		// Act 2
		MediumDto result = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(mediumDto)
			.when()
			.post("v1")
			.then()
			.statusCode(201)
			.and()
			.extract()
			.as(MediumDto.class);

		// Assert 1
		assertEquals(Medienart.ZEITSCHRIFT, result.getMedienart());
		assertFalse("neu".equals(result.getId()));
		assertEquals("Leipziger Volkszeitung", result.getTitel());
		assertEquals("https://mathematikalpha.de/", result.getUrl());
		assertEquals("Knobelaufgaben", result.getKommentar());
		assertEquals("/media/veracrypt2/mathe/zeitschriften/lvz/lvz1.pdf", result.getPfad());

		String id = result.getId();

		// Act 2
		MediumDto[] treffermenge1 = given()
			.accept(ContentType.JSON)
			.queryParam("suchstring", "volk")
			.when()
			.get("titel/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediumDto[].class);

		// Assert 2
		assertEquals(1, treffermenge1.length);
		assertEquals(id, treffermenge1[0].getId());

		// Arrange 3
		Mediensuchmodus suchmodusNoop = Mediensuchmodus.NOOP;

		MediensucheTreffer mediensucheTreffer = given()
			.queryParam("suchmodus", suchmodusNoop)
			.queryParam("suchstring", "aufgabe")
			.queryParam("limit", 3)
			.queryParam("offset", 0)
			.when()
			.get("v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediensucheTreffer.class);

		// Assert 3
		assertEquals(1, mediensucheTreffer.getTrefferGesamt());

		List<MediensucheTrefferItem> treffermenge2 = mediensucheTreffer.getTreffer();
		assertEquals(1, treffermenge2.size());
		assertEquals(id, treffermenge2.get(0).getId());

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(2)
	void should_mediumAnlegenReturn409_when_TitelGleich() {

		// Arrange
		MediumDto mediumDto = new MediumDto()
			.withTitel("Leipziger Volkszeitung")
			.withId("neu").withKommentar("Knobelaufgaben")
			.withMedienart(Medienart.ZEITSCHRIFT)
			.withUrl("https://mathematikalpha.de/")
			.withPfad("/media/veracrypt2/mathe/zeitschriften/lvz/lvz1.pdf");

		// Act 2
		MessagePayload result = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(mediumDto)
			.when()
			.post("v1")
			.then()
			.statusCode(409)
			.and()
			.extract()
			.as(MessagePayload.class);

		// Assert 1
		assertEquals("ERROR", result.getLevel());
		assertEquals("Der Titel ist bereits vergeben.", result.getMessage());
	}
}
