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
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import jakarta.persistence.EnumType;

/**
 * PublicRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class PublicRaetselResourceTest {

	private String deskriptoren = "8,11";

	@Test
	@Order(1)
	void findRaetselPublic_401() throws Exception {

		given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("modusDeskriptoren", SuchmodusDeskriptoren.LIKE)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(401);
	}

	@Test
	@TestSecurity(user = "standard", roles = { "STANDARD" })
	@Order(2)
	void findRaetselPublic_when_Deskriptoren_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("modusDeskriptoren", SuchmodusDeskriptoren.LIKE)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		// Das ist irriterend, weil man die Roles benötigt, damit man nicht sofort eine 401 bekommt, aber wegen mockSession ein
		// adminUser in den authContext gesetzt wird. Das Ergebnis ist daher nicht auf die FREIGEGEBENEN beschränkt
		assertEquals(13, alleRaetsel.size());
		assertEquals(13, suchergebnis.getTrefferGesamt());

		assertEquals("02540", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "standard", roles = { "STANDARD" })
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

		// Das ist irriterend, weil man die Roles benötigt, damit man nicht sofort eine 401 bekommt, aber wegen mockSession ein
		// adminUser in den authContext gesetzt wird. Das Ergebnis ist daher nicht auf die FREIGEGEBENEN beschränkt
		assertEquals(13, alleRaetsel.size());
		assertEquals(13, suchergebnis.getTrefferGesamt());

		assertEquals("02540", alleRaetsel.get(0).getSchluessel());
	}

	@Test
	@TestSecurity(user = "standard", roles = { "STANDARD" })
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
	@TestSecurity(user = "standard", roles = { "STANDARD" })
	@Order(5)
	void findRaetselPublic_when_Deskriptoren_NOT_LIKE() throws Exception {

		RaetselsucheTreffer suchergebnis = given()
			.queryParam("deskriptoren", deskriptoren)
			.queryParam("modusDeskriptoren", SuchmodusDeskriptoren.NOT_LIKE)
			.queryParam("typeDeskriptoren", EnumType.ORDINAL)
			.when()
			.get("v2")
			.then()
			.statusCode(200)
			.and()
			.extract()
			.as(RaetselsucheTreffer.class);

		List<RaetselsucheTrefferItem> alleRaetsel = suchergebnis.getTreffer();

		// Das ist irriterend, weil man die Roles benötigt, damit man nicht sofort eine 401 bekommt, aber wegen mockSession ein
		// adminUser in den authContext gesetzt wird. Das Ergebnis ist daher nicht auf die FREIGEGEBENEN beschränkt
		assertEquals(20, alleRaetsel.size());

		// Je nach Testkontext kann ein Rätsel hinzugekommen sein.
		assertTrue(suchergebnis.getTrefferGesamt() >= 50);

		assertEquals("00000", alleRaetsel.get(0).getSchluessel());
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

		// Assert
		// Das ist irriterend, weil man die Roles benötigt, damit man nicht sofort eine 401 bekommt, aber wegen mockSession ein
		// adminUser in den authContext gesetzt wird. Das Ergebnis ist daher nicht auf die FREIGEGEBENEN beschränkt

		// Je nach Testkontext kann ein Rätsel hinzugekommen sein.
		assertTrue(result.getErgebnis() >= 31);
	}

}
