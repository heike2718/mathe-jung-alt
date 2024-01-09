// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.egladil.mja_api.domain.SuchmodusDeskriptoren;
import de.egladil.mja_api.domain.dto.AnzahlabfrageResponseDto;
import de.egladil.mja_api.domain.raetsel.dto.AufgabensammlungRaetselsucheTrefferItem;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.profiles.FullDatabaseStandarduserTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.persistence.EnumType;

/**
 * PublicRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseStandarduserTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class PublicRaetselResourceTest {

	private String deskriptoren = "8,11";

	@Test
	@Order(1)
	void findRaetselPublic_401() throws Exception {

		given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.LIKE)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	@Order(2)
	void findRaetselPublic_when_Deskriptoren_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.LIKE)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(6, alleRaetsel.size());
		assertEquals(6, suchergebnis.getTrefferGesamt());

		assertEquals("02604", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	@Order(3)
	void findRaetselPublic_when_fallbackToDefaultModusDeskriptoren() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(6, alleRaetsel.size());
		assertEquals(6, suchergebnis.getTrefferGesamt());

		assertEquals("02604", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	@Order(4)
	void findRaetselPublic_when_deskriptorenBlank() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", "")
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();
		assertEquals(0, alleRaetsel.size());
		assertEquals(0, suchergebnis.getTrefferGesamt());
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	@Order(5)
	void findRaetselPublic_when_Deskriptoren_NOT_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("searchModeForDescriptors", SuchmodusDeskriptoren.NOT_LIKE)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		assertEquals(20, alleRaetsel.size());

		assertTrue(suchergebnis.getTrefferGesamt() >= 6);

		assertEquals("01219", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@Order(6)
	void testGetAnzahlFreigegebeneRaetsel() throws Exception {

		AnzahlabfrageResponseDto result = given()
			.when()
			.get("/public/anzahl/v1")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(AnzahlabfrageResponseDto.class);

		// Je nach Testkontext kann ein Rätsel hinzugekommen sein.
		assertTrue(result.getErgebnis() >= 31);
	}

	@Test
	@TestSecurity(user = "testuser", roles = { "STANDARD" })
	@Order(7)
	void testGetAufgabensammlungenMitRaetsel_return_only_freigegeben_when_standardUser() {

		// Arrange
		String raetselId = "08dc5237-505d-4db2-b5f9-3fd3d74981e0";

		// Act
		AufgabensammlungRaetselsucheTrefferItem[] result = given()
			.pathParam("id", raetselId)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.when()
			.get("{id}/aufgabensammlungen/v1")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(AufgabensammlungRaetselsucheTrefferItem[].class);

		// Assert
		assertEquals(0, result.length);

	}

	@Test
	@Order(8)
	void testGetAufgabensammlungenMitRaetsel_401_when_unknownUser() {

		// Arrange
		String raetselId = "2c6fc5a1-f27c-4d51-98c4-239a1eead05f";

		// Act
		given()
			.pathParam("id", raetselId)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.when()
			.get("{id}/aufgabensammlungen/v1")
			.then()
			.statusCode(401);

	}
}
