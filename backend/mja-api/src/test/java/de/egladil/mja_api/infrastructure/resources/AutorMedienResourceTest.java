// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.medien.Mediensuchmodus;
import de.egladil.mja_api.domain.medien.dto.MediensucheResult;
import de.egladil.mja_api.domain.medien.dto.MediensucheTrefferItem;
import de.egladil.mja_api.domain.medien.dto.MediumDto;
import de.egladil.mja_api.domain.medien.dto.MediumQuelleDto;
import de.egladil.mja_api.domain.medien.dto.RaetselMediensucheTrefferItem;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.profiles.FullDatabaseAutorTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * AutorMedienResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(MedienResource.class)
@TestProfile(FullDatabaseAutorTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class AutorMedienResourceTest {

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(1)
	void should_mediumAendernReturn403Medium_when_notTheOwner() throws Exception {

		// Arrange
		String id = "9ab888be-e84b-4c81-ab4d-4451a5097892";

		MediumDto mediumDto = new MediumDto()
			.withTitel("Leipziger Volkszeitung")
			.withId(id)
			.withKommentar("von der Leipziger Volkszeitung herausgegebene Sonderhefte mit Knobelaufgaben")
			.withMedienart(Medienart.ZEITSCHRIFT)
			.withUrl("https://mathematikalpha.de/");

		// Act + Assert
		given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(mediumDto)
			.when()
			.put("v1")
			.then()
			.statusCode(403);
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(2)
	void should_findAllReturnAllMedien() {

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
		long trefferGesamt = mediensucheTreffer.getTrefferGesamt();

		assertTrue(trefferGesamt >= 8);

		List<MediensucheTrefferItem> treffermenge = mediensucheTreffer.getTreffer();
		assertTrue(treffermenge.size() >= 8);

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
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(3)
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
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(4)
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
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(5)
	void should_LoadDetailsWork_when_notTheOwner() throws Exception {

		// Arrange
		String id = "9ab888be-e84b-4c81-ab4d-4451a5097892";

		// Act + Assert
		MediumDto result = given()
			.pathParam("id", id)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.when()
			.get("{id}/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediumDto.class);

		assertFalse(result.isOwnMedium());
		assertTrue(result.isSchreibgeschuetzt());
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(6)
	void should_getRaetselMitMedium_work() {

		// Arrange
		String mediumId = "2c6fc5a1-f27c-4d51-98c4-239a1eead05f";

		// Act
		RaetselMediensucheTrefferItem[] trefferliste = given()
			.pathParam("id", mediumId)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.when()
			.get("{id}/raetsel/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(RaetselMediensucheTrefferItem[].class);

		// Assert
		assertEquals(2, trefferliste.length);

		{

			RaetselMediensucheTrefferItem item = trefferliste[0];
			assertEquals("46b8bd51-9cd6-4d06-9b5b-26495b5e5a4c", item.getId());
			assertEquals("02821", item.getSchluessel());
			assertEquals(RaetselHerkunftTyp.ADAPTION, item.getHerkunftstyp());
			assertEquals("Kängurus springen 6 Mal", item.getName());
			assertEquals(
				"Frodo Beutlin aus Beutelsend (basierend auf einer Idee aus Dokumentation Johannes Lehmann: 25 Jahre ABC-Mathematikolympiaden, S.47)",
				item.getQuellenangabe());
			assertEquals("/media/veracrypt2/mathe/wettbewerbe/grundschule/abc_olympiade.pdf", item.getPfad());
		}

		{

			RaetselMediensucheTrefferItem item = trefferliste[1];
			assertEquals("f61b41c9-1d00-45f5-8d47-c2fa11ca541e", item.getId());
			assertEquals("02822", item.getSchluessel());
			assertEquals(RaetselHerkunftTyp.ZITAT, item.getHerkunftstyp());
			assertEquals("Ziffern in einem Satz zählen", item.getName());
			assertEquals("Dokumentation Johannes Lehmann: 25 Jahre ABC-Mathematikolympiaden, S.51", item.getQuellenangabe());
			assertEquals("/media/veracrypt2/mathe/wettbewerbe/grundschule/abc_olympiade.pdf", item.getPfad());
		}

	}
}
