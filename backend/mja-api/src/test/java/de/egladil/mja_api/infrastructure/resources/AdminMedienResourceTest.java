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
import de.egladil.mja_api.domain.medien.dto.MediensucheResult;
import de.egladil.mja_api.domain.medien.dto.MediensucheTrefferItem;
import de.egladil.mja_api.domain.medien.dto.MediumDto;
import de.egladil.mja_api.domain.medien.dto.MediumQuelleDto;
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
 * AdminMedienResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(MedienResource.class)
@TestProfile(FullDatabaseAdminTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class AdminMedienResourceTest {

	@Inject
	MediumDao mediumDao;

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(1)
	void should_sucheAlle_work() {

		// Arrange 3
		Mediensuchmodus suchmodusNoop = Mediensuchmodus.NOOP;

		MediensucheResult mediensucheTreffer = given()
			.queryParam("suchmodus", suchmodusNoop)
			.queryParam("limit", 20)
			.queryParam("offset", 0)
			.when()
			.get("v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediensucheResult.class);

		// Assert 3
		assertEquals(9, mediensucheTreffer.getTrefferGesamt());

		List<MediensucheTrefferItem> treffermenge = mediensucheTreffer.getTreffer();
		assertEquals(9, treffermenge.size());

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "5f9bc03c-84f5-48ea-ab6c-ddc265f5d963".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "6cbf3a2e-0218-4123-8850-6a3d629dee0a".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "96d02c3f-c0a6-4bfc-bf08-77190c8de297".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "af9f75bd-9340-40d0-a3dd-0594b6ba0632".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}

		// ///////////////////////////////////////

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "4f2e96ae-002c-4530-a873-a9cfc65814ff".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "9ab888be-e84b-4c81-ab4d-4451a5097892".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "a94879e6-7479-4c2d-a061-34709d8f9631".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}

		{

			Optional<MediensucheTrefferItem> optItem = treffermenge.stream()
				.filter(m -> "dbf00c75-6c97-4a1c-afe6-a42462a44e39".equals(m.getId())).findFirst();
			assertTrue(optItem.isPresent());
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(2)
	void should_sucheMitSuchstring_work() {

		// Arrange
		Mediensuchmodus suchmodusNoop = Mediensuchmodus.SEARCHSTRING;

		// Act
		MediensucheResult mediensucheTreffer = given()
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
			.as(MediensucheResult.class);

		// Assert
		assertEquals(1, mediensucheTreffer.getTrefferGesamt());

		List<MediensucheTrefferItem> treffermenge = mediensucheTreffer.getTreffer();
		assertEquals(1, treffermenge.size());
		assertEquals("9ab888be-e84b-4c81-ab4d-4451a5097892", treffermenge.get(0).getId());

	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(3)
	void should_findMedienForUseInQuelle_work_whenBuch() {

		// Act
		MediumQuelleDto[] result = given()
			.queryParam("medienart", Medienart.BUCH)
			.when()
			.get("quelle/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediumQuelleDto[].class);

		// Assert
		assertEquals(4, result.length);

		{

			MediumQuelleDto medium = result[0];
			assertEquals("6cbf3a2e-0218-4123-8850-6a3d629dee0a", medium.getId());
		}

		{

			MediumQuelleDto medium = result[1];
			assertEquals("2c6fc5a1-f27c-4d51-98c4-239a1eead05f", medium.getId());
		}

		{

			MediumQuelleDto medium = result[2];
			assertEquals("dbf00c75-6c97-4a1c-afe6-a42462a44e39", medium.getId());
		}

		{

			MediumQuelleDto medium = result[3];
			assertEquals("5f9bc03c-84f5-48ea-ab6c-ddc265f5d963", medium.getId());
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(4)
	void should_findMedienForUseInQuelle_work_whenInternet() {

		// Act
		MediumQuelleDto[] result = given()
			.queryParam("medienart", Medienart.INTERNET)
			.when()
			.get("quelle/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediumQuelleDto[].class);

		// Assert
		assertEquals(2, result.length);

		{

			MediumQuelleDto medium = result[0];
			assertEquals("4f2e96ae-002c-4530-a873-a9cfc65814ff", medium.getId());
		}

		{

			MediumQuelleDto medium = result[1];
			assertEquals("af9f75bd-9340-40d0-a3dd-0594b6ba0632", medium.getId());
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(10)
	void should_mediumAnlegenAndFindenWork_when_Zeitschrift() {

		// Arrange
		MediumDto mediumDto = new MediumDto()
			.withTitel("quant")
			.withId("neu").withKommentar("russische Zeitschrift für Mathematik und Physik")
			.withMedienart(Medienart.ZEITSCHRIFT);

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
		assertEquals("quant", result.getTitel());
		assertNull(result.getUrl());
		assertEquals("russische Zeitschrift für Mathematik und Physik", result.getKommentar());

		String id = result.getId();

		// Act 2
		MediumQuelleDto[] treffermenge1 = given()
			.accept(ContentType.JSON)
			.queryParam("medienart", Medienart.ZEITSCHRIFT)
			.when()
			.get("quelle/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediumQuelleDto[].class);

		// Assert 2
		assertEquals(4, treffermenge1.length);

		{

			MediumQuelleDto medium = treffermenge1[0];
			assertEquals("a94879e6-7479-4c2d-a061-34709d8f9631", medium.getId());
		}

		{

			MediumQuelleDto medium = treffermenge1[1];
			assertEquals("9ab888be-e84b-4c81-ab4d-4451a5097892", medium.getId());
		}

		{

			MediumQuelleDto medium = treffermenge1[2];
			assertEquals(id, medium.getId());
		}

		{

			MediumQuelleDto medium = treffermenge1[3];
			assertEquals("96d02c3f-c0a6-4bfc-bf08-77190c8de297", medium.getId());
		}
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(11)
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
	@Order(12)
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
		assertFalse(ausDB.owner.equals(ausDB.geaendertDurch));
	}

	@Test
	@TestSecurity(user = "admin", roles = { "ADMIN" })
	@Order(13)
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
	@Order(14)
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

}
