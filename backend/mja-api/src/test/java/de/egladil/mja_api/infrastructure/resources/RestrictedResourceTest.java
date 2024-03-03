// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.MjaApiApplication;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.s2s.MkGatewayAuthConfig;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruAufgabe;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruAufgabenDto;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

/**
 * RestrictedResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RestrictedResource.class)
@TestProfile(FullDatabaseAdminTestProfile.class)
public class RestrictedResourceTest {

	@Inject
	MkGatewayAuthConfig authConfig;

	@Test
	void should_getAufgabenMinikaenguruwettbewerb_return401_when_authHeaderInvalid() {

		MessagePayload messagePayload = given()
			.header(MjaApiApplication.X_CLIENT_ID_HEADER_NAME, authConfig.client())
			.header("Authorization", getInvalidAuthHeader())
			.when().get("/minikaenguru/2021/ZWEI")
			.then()
			.statusCode(401)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals("keine Berechtigung: S2S-Authentifizierung fehlgeschlagen. Bitte konfigurierten Authorization-Header pruefen.",
			messagePayload.getMessage());
	}

	@Test
	void should_getAufgabenMinikaenguruwettbewerb_return401_when_correctAuthHeaderButDifferentClientId() {

		MessagePayload messagePayload = given()
			.header(MjaApiApplication.X_CLIENT_ID_HEADER_NAME, "hallo")
			.header("Authorization", new String(Base64.getEncoder().encode(authConfig.header().getBytes())))
			.when().get("/minikaenguru/2021/ZWEI")
			.then()
			.statusCode(401)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals(
			"keine Berechtigung: S2S-Authentifizierung fehlgeschlagen. Bitte Konfiguration von mk-gateway.auth.client und Header X-CLIENT-ID pruefen.",
			messagePayload.getMessage());
	}

	@Test
	void should_getAufgabenMinikaenguruwettbewerb_return404_when_schwierigkeitsgradNotAsExpected() {

		List<Schwierigkeitsgrad> schwierigkeitsgrade = Arrays.stream(Schwierigkeitsgrad.values())
			.filter(s -> !s.isValidForMinikaenguruResources()).toList();

		Random random = new Random();
		Integer index = random.nextInt(schwierigkeitsgrade.size());

		Schwierigkeitsgrad schwierigkeitsgrad = schwierigkeitsgrade.get(index.intValue());

		MessagePayload messagePayload = given()
			.header(MjaApiApplication.X_CLIENT_ID_HEADER_NAME, authConfig.client())
			.header("Authorization", new String(Base64.getEncoder().encode(authConfig.header().getBytes())))
			.when().get("/minikaenguru/2020/" + schwierigkeitsgrad.toString())
			.then()
			.statusCode(400)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals(
			"Es gibt keine Aufgaben für den angefragten Schwierigkeitsgrad",
			messagePayload.getMessage());
	}

	@Test
	void should_getAufgabenMinikaenguruwettbewerb_return404_when_keinTreffer() {

		Schwierigkeitsgrad schwierigkeitsgrad = Schwierigkeitsgrad.EINS;

		MessagePayload messagePayload = given()
			.header(MjaApiApplication.X_CLIENT_ID_HEADER_NAME, authConfig.client())
			.header("Authorization", new String(Base64.getEncoder().encode(authConfig.header().getBytes())))
			.when().get("/minikaenguru/2016/" + schwierigkeitsgrad.toString())
			.then()
			.statusCode(404)
			.extract()
			.as(MessagePayload.class);

		assertEquals("ERROR", messagePayload.getLevel());
		assertEquals(
			"Es gibt keine Minikänguru-Aufgaben mit jahr und schwierigkeitsgrad.",
			messagePayload.getMessage());
	}

	@Test
	void should_getAufgabenMinikaenguruwettbewerb_return200_when_everythingIsOK() {

		Schwierigkeitsgrad schwierigkeitsgrad = Schwierigkeitsgrad.EINS;

		MinikaenguruAufgabenDto responsePayload = given()
			.header(MjaApiApplication.X_CLIENT_ID_HEADER_NAME, authConfig.client())
			.header("Authorization", new String(Base64.getEncoder().encode(authConfig.header().getBytes())))
			.when().get("/minikaenguru/2020/" + schwierigkeitsgrad.toString())
			.then()
			.statusCode(200)
			.extract()
			.as(MinikaenguruAufgabenDto.class);

		assertEquals("2020", responsePayload.getWettbewerbsjahr());
		assertEquals("Klasse 1", responsePayload.getKlassenstufe());

		List<MinikaenguruAufgabe> aufgaben = responsePayload.getAufgaben();

		assertEquals(12, aufgaben.size());

		{

			MinikaenguruAufgabe aufgabe = aufgaben.get(0);
			assertEquals("A-1", aufgabe.getNummer());
			assertEquals(3, aufgabe.getPunkte());
			assertEquals("E", aufgabe.getLoesungsbuchstabe());
			Images images = aufgabe.getImages();
			assertNotNull(images.getImageFrage());
			assertNotNull(images.getImageLoesung());
		}

		{

			MinikaenguruAufgabe aufgabe = aufgaben.get(11);
			assertEquals("C-4", aufgabe.getNummer());
			assertEquals(5, aufgabe.getPunkte());
			assertEquals("D", aufgabe.getLoesungsbuchstabe());
			Images images = aufgabe.getImages();
			assertNotNull(images.getImageFrage());
			assertNotNull(images.getImageLoesung());
		}
	}

	private String getInvalidAuthHeader() {

		String headerToEncode = authConfig.client() + ":hallo";

		return new String(Base64.getEncoder().encode(headerToEncode.getBytes()));
	}

}
