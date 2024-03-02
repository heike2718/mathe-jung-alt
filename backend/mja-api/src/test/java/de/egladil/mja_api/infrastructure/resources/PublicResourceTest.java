// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruAufgabe;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruAufgabenDto;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

/**
 * PublicResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(PublicResource.class)
@TestProfile(FullDatabaseAdminTestProfile.class)
public class PublicResourceTest {

	@Test
	void should_getAufgabenMinikaenguruwettbewerb_return404_when_schwierigkeitsgradNotAsExpected() {

		List<Schwierigkeitsgrad> schwierigkeitsgrade = Arrays.stream(Schwierigkeitsgrad.values())
			.filter(s -> !s.isValidForMinikaenguruResources()).toList();

		Random random = new Random();
		Integer index = random.nextInt(schwierigkeitsgrade.size());

		Schwierigkeitsgrad schwierigkeitsgrad = schwierigkeitsgrade.get(index.intValue());

		MessagePayload messagePayload = given()
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
			.when().get("/minikaenguru/2017/" + schwierigkeitsgrad.toString())
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
	void should_getAufgabenMinikaenguruwettbewerb_return404_when_nichtFreigegeben() {

		Schwierigkeitsgrad schwierigkeitsgrad = Schwierigkeitsgrad.ZWEI;

		MessagePayload messagePayload = given()
			.when().get("/minikaenguru/2022/" + schwierigkeitsgrad.toString())
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
}
