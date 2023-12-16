// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

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
import de.egladil.mja_api.infrastructure.persistence.dao.MediumDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesMedium;
import de.egladil.mja_api.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

/**
 * MedienResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(MedienResource.class)
@TestProfile(FullDatabaseAdminTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class MedienResourceTest {

	@Inject
	MediumDao mediumDao;

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(1)
	void should_mediumAnlegenAndFindenWork_when_Zeitschrift() {

		// Arrange
		MediumDto mediumDto = new MediumDto()
			.withTitel("alpha")
			.withId("neu").withKommentar("mathematische Schülerzeitschrift")
			.withMedienart(Medienart.ZEITSCHRIFT)
			.withUrl("https://mathematikalpha.de/");

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
		assertEquals("alpha", result.getTitel());
		assertEquals("https://mathematikalpha.de/", result.getUrl());
		assertEquals("mathematische Schülerzeitschrift", result.getKommentar());

		String id = result.getId();

		// Act 2
		MediumDto[] treffermenge1 = given()
			.accept(ContentType.JSON)
			.queryParam("suchstring", "alph")
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
			.withUrl("https://mathematikalpha.de/");

		// Act
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

		// Assert
		assertEquals("ERROR", result.getLevel());
		assertEquals("Der Titel ist bereits vergeben.", result.getMessage());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(3)
	void should_mediumAendernNotChangeOwner() {

		// Arrange
		String id = "6cbf3a2e-0218-4123-8850-6a3d629dee0a";

		MediumDto mediumDto = new MediumDto()
			.withTitel("2 x 3 plus Spaß dabei")
			.withId(id)
			.withMedienart(Medienart.BUCH)
			.withAutor("Johannes Lehmann");

		// Act
		MediumDto result = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(mediumDto)
			.when()
			.put("v1")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(MediumDto.class);

		// Assert
		assertEquals(Medienart.BUCH, result.getMedienart());
		assertFalse("neu".equals(result.getId()));
		assertEquals("2 x 3 plus Spaß dabei", result.getTitel());
		assertEquals("Johannes Lehmann", result.getAutor());
		assertNull(result.getUrl());
		assertNull(result.getKommentar());

		PersistentesMedium ausDB = mediumDao.findMediumById(id);
		assertEquals("412b67dc-132f-465a-a3c3-468269e866cb", ausDB.owner);
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(4)
	void should_mediumAendernReturn409_when_titelExists() {

		// Arrange
		String id = "9ab888be-e84b-4c81-ab4d-4451a5097892";

		MediumDto mediumDto = new MediumDto()
			.withTitel("alpha")
			.withId(id)
			.withKommentar("von der Leipziger Volkszeitung herausgegebene Sonderhefte mit Knobelaufgaben")
			.withMedienart(Medienart.ZEITSCHRIFT)
			.withUrl("https://mathematikalpha.de/");

		// Act
		MessagePayload result = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(mediumDto)
			.when()
			.put("v1")
			.then()
			.statusCode(409)
			.and()
			.extract()
			.as(MessagePayload.class);

		// Assert
		assertEquals("ERROR", result.getLevel());
		assertEquals("Der Titel ist bereits vergeben.", result.getMessage());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(4)
	void should_mediumAendernReturn404_when_uuidUnknown() {

		// Arrange
		String id = "00000000-0000-0000-0000-000000000000";

		MediumDto mediumDto = new MediumDto()
			.withTitel("kvant")
			.withId(id)
			.withKommentar("russische mathematische Schülerzeitschrift")
			.withMedienart(Medienart.ZEITSCHRIFT);

		// Act
		MessagePayload result = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(mediumDto)
			.when()
			.put("v1")
			.then()
			.statusCode(404)
			.and()
			.extract()
			.as(MessagePayload.class);

		// Assert
		assertEquals("ERROR", result.getLevel());
		assertEquals("Das Medium existiert nicht.", result.getMessage());
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(10)
	void should_sucheMitSuchstring_work() {

		// Arrange
		Mediensuchmodus suchmodusNoop = Mediensuchmodus.SEARCHSTRING;

		// Act
		MediensucheTreffer mediensucheTreffer = given()
			.queryParam("suchmodus", suchmodusNoop)
			.queryParam("suchstring", "Knobel")
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

		// Assert
		assertEquals(1, mediensucheTreffer.getTrefferGesamt());

		List<MediensucheTrefferItem> treffermenge = mediensucheTreffer.getTreffer();
		assertEquals(1, treffermenge.size());
		assertEquals("9ab888be-e84b-4c81-ab4d-4451a5097892", treffermenge.get(0).getId());

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(11)
	void should_sucheAlle_work() {

		// Arrange 3
		Mediensuchmodus suchmodusNoop = Mediensuchmodus.NOOP;

		MediensucheTreffer mediensucheTreffer = given()
			.queryParam("suchmodus", suchmodusNoop)
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
		assertEquals(3, mediensucheTreffer.getTrefferGesamt());

		List<MediensucheTrefferItem> treffermenge = mediensucheTreffer.getTreffer();
		assertEquals(3, treffermenge.size());

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream().filter(m -> "alpha".equals(m.getTitel())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "Leipziger Volkszeitung".equals(m.getTitel())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "2 x 3 plus Spaß dabei".equals(m.getTitel())).findFirst();
			assertTrue(optItem.isPresent());
		}
	}
}
