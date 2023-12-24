// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
	void should_mediumAendernReturn403_when_notTheOwner() throws Exception {

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
	void should_findAllReturnOnlyOwnMedien() {

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

		assertEquals(4, trefferGesamt);

		List<MediensucheTrefferItem> treffermenge = mediensucheTreffer.getTreffer();
		assertEquals(4, treffermenge.size());

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
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(3)
	void should_findMedienForUseInQuelle_returnOnlyOwnMedien() {

		// Arrange
		Mediensuchmodus suchmodusNoop = Mediensuchmodus.SEARCHSTRING;

		// Act
		MediumDto[] result = given()
			.queryParam("suchmodus", suchmodusNoop)
			.queryParam("suchstring", "Grundschul")
			.when()
			.get("titel/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediumDto[].class);

		// Assert
		assertEquals(0, result.length);
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(4)
	void should_findMedienForUseInQuelle_work() {

		// Arrange
		Mediensuchmodus suchmodusNoop = Mediensuchmodus.SEARCHSTRING;

		// Act
		MediumDto[] result = given()
			.queryParam("suchmodus", suchmodusNoop)
			.queryParam("suchstring", "arith")
			.when()
			.get("titel/v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(MediumDto[].class);

		// Assert
		assertEquals(1, result.length);

		MediumDto medium = result[0];
		assertEquals("5f9bc03c-84f5-48ea-ab6c-ddc265f5d963", medium.getId());
	}
}
