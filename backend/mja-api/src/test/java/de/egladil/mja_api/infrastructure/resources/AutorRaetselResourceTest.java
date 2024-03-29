// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.auth.config.AuthConstants;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.domain.raetsel.dto.AufgabensammlungRaetselsucheTrefferItem;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.infrastructure.persistence.dao.MediumDao;
import de.egladil.mja_api.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesMedium;
import de.egladil.mja_api.profiles.FullDatabaseAutorTestProfile;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

/**
 * AutorRaetselResourceTest
 */
@QuarkusTest
@TestHTTPEndpoint(RaetselResource.class)
@TestProfile(FullDatabaseAutorTestProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class AutorRaetselResourceTest {

	private static final String CSRF_TOKEN = "lqhidhqio";

	@Inject
	MediumDao mediumDao;

	@Inject
	QuellenRepository quellenRepo;

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
	void testRaetselDetailsLadenAndererOwner() throws Exception {

		Raetsel treffer = given()
			.when().get("02606/v1").then()
			.statusCode(200)
			.and()
			.extract()
			.as(Raetsel.class);

		assertEquals("0ce69e0e-e1f8-4400-a2b9-61d3d6b0a82e", treffer.getId());
		assertEquals("02606", treffer.getSchluessel());
		assertTrue(treffer.isFreigegeben());
		assertEquals(RaetselHerkunftTyp.EIGENKREATION, treffer.getHerkunftstyp());

		QuelleDto quelle = treffer.getQuelle();
		assertEquals("Heike Winkelvoß", treffer.getQuellenangabe());
		assertEquals(Quellenart.PERSON, quelle.getQuellenart());
		assertNull(quelle.getMediumUuid());
		assertEquals("8ef4d9b8-62a6-4643-8674-73ebaec52d98", quelle.getId());
		assertTrue(treffer.isSchreibgeschuetzt());
	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(3)
	void should_raetselAnlegenMitQuelleZeitschrift_undAendernMitQuelle_work() {

		// Arrange anlegen
		String mediumUuid = "9ab888be-e84b-4c81-ab4d-4451a5097892";
		EditRaetselPayload editRaetselPayload = createPayloadAnlegen(mediumUuid);

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

		assertNotNull(result.getId());
		assertEquals("Frodo Beutlin aus Beutelsend (basierend auf einer Idee aus Leipziger Volkszeitung 1965, S.32)",
			result.getQuellenangabe());

		String raetselId = result.getId();
		assertEquals(RaetselHerkunftTyp.ADAPTION, result.getHerkunftstyp());

		QuelleDto quelle = result.getQuelle();
		assertEquals(Quellenart.ZEITSCHRIFT, quelle.getQuellenart());
		assertEquals(mediumUuid, quelle.getMediumUuid());
		assertEquals(editRaetselPayload.getQuelle().getPfad(), quelle.getPfad());

		String quelleId = quelle.getId();
		System.out.println("quelleID=" + quelleId);

		assertFalse("neu".equals(quelleId));

		// Arrange ändern
		mediumUuid = "6cbf3a2e-0218-4123-8850-6a3d629dee0a"; // das ist jetzt ein Buch
		EditRaetselPayload payloadAenderung = createPayloadAendern(result.getSchluessel(), raetselId, quelleId, mediumUuid);

		System.out.println("raetselId=" + payloadAenderung.getId());
		System.out.println("quelleId=" + payloadAenderung.getQuelle().getId());

		PersistentesMedium theMedium = mediumDao.findMediumById(mediumUuid);

		assertNotNull(theMedium);

		// Im Laufe der Tests wird dieses Medium umbenannt. Daher hier den aktuellen titel holen.
		String expectedHerkunftText = theMedium.autor + ": " + theMedium.titel + ", S.16";

		// Act ändern
		result = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payloadAenderung)
			.put("v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(Raetsel.class);

		// Assert ändern
		QuelleDto geaenderteQuelle = result.getQuelle();
		assertEquals(expectedHerkunftText, result.getQuellenangabe());
		assertEquals(quelleId, geaenderteQuelle.getId());
		assertEquals(mediumUuid, geaenderteQuelle.getMediumUuid());

		// zurück auf EIGENKREATION
		payloadAenderung.withId(raetselId).withHerkunftstyp(RaetselHerkunftTyp.EIGENKREATION);

		result = given()
			.header(AuthConstants.CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN)
			.cookie(AuthConstants.CSRF_TOKEN_COOKIE_NAME, CSRF_TOKEN)
			.contentType(ContentType.JSON)
			.body(payloadAenderung)
			.put("v1")
			.then()
			.statusCode(200)
			.and()
			.contentType(ContentType.JSON)
			.extract()
			.as(Raetsel.class);

		geaenderteQuelle = result.getQuelle();
		assertEquals("73634aeb-f494-4864-ab30-26861a5bf2e0", geaenderteQuelle.getId());

		assertNull(quellenRepo.findQuelleEntityWithId(quelleId));

	}

	@Test
	@TestSecurity(user = "autor", roles = { "AUTOR" })
	@Order(4)
	void should_findAufgabensammlungenWithRaetsel_work() {

		// Arrange
		String raetselId = "e6f4c722-fcc2-4695-91df-5e0e1bd5cddb";

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
		assertEquals(2, result.length);

		{

			AufgabensammlungRaetselsucheTrefferItem item = result[0];
			assertEquals("1c34d95c-dce4-4e38-b6c0-bd6072bb4232", item.getId());
			assertEquals("Minikänguru 2020 - Klasse 1", item.getName());
			assertEquals("B-1", item.getNummer());
			assertEquals(400, item.getPunkte());
			assertEquals(Schwierigkeitsgrad.EINS, item.getSchwierigkeitsgrad());
			assertTrue(item.isFreigegeben());
			assertFalse(item.isPrivat());
			assertEquals("b865fc75...", item.getOwner());
		}

		{

			AufgabensammlungRaetselsucheTrefferItem item = result[1];
			assertEquals("c09e5d63-9ec1-4884-a01e-08234db9cbf3", item.getId());
			assertEquals("Minikänguru 2020 - Klasse 2", item.getName());
			assertEquals("A-3", item.getNummer());
			assertEquals(300, item.getPunkte());
			assertEquals(Schwierigkeitsgrad.ZWEI, item.getSchwierigkeitsgrad());
			assertTrue(item.isFreigegeben());
			assertFalse(item.isPrivat());
			assertEquals("b865fc75...", item.getOwner());
		}

	}

	private EditRaetselPayload createPayloadAnlegen(final String mediumId) {

		String raetselId = "neu";
		String quelleId = "neu";

		QuelleDto quelle = new QuelleDto();
		quelle.setMediumUuid(mediumId);
		quelle.setPfad("/media/veracrypt2/mathe/zeitschriften/lvz/lvz1.pdf");
		quelle.setSeite("32");
		quelle.setJahr("1965");
		quelle.setQuellenart(Quellenart.ZEITSCHRIFT);
		quelle.setId(quelleId);

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

		EditRaetselPayload editRaetselPayload = new EditRaetselPayload()
			.withId(raetselId)
			.withDeskriptoren(deskriptoren)
			.withFrage(
				"Subtrahiere von der kleinsten Zahl mit 4 verschiedenen Ziffern die größte Zahl mit 2 verschiedenen Ziffern.")
			.withKommentar("Ziffern versus Zahlen")
			.withLoesung("$1234 - 98 = 1136")
			.withName("Subtraktion und Ziffernverständnis")
			.withHerkunftstyp(RaetselHerkunftTyp.ADAPTION)
			.withQuelle(quelle);

		return editRaetselPayload;

	}

	private EditRaetselPayload createPayloadAendern(final String schluessel, final String raetselId, final String quelleId, final String mediumId) {

		QuelleDto quelle = new QuelleDto();
		quelle.setMediumUuid(mediumId);
		quelle.setSeite("16");
		quelle.setQuellenart(Quellenart.BUCH);
		quelle.setId(quelleId);

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

		EditRaetselPayload editRaetselPayload = new EditRaetselPayload()
			.withId(raetselId)
			.withSchluessel(schluessel)
			.withDeskriptoren(deskriptoren)
			.withFrage(
				"Subtrahiere von der kleinsten Zahl mit 4 verschiedenen Ziffern die größte Zahl mit 2 verschiedenen Ziffern.")
			.withKommentar("Quelle auf Buch geändert und Herkunft auf ZITAT")
			.withLoesung("$1234 - 98 = 1136")
			.withName("Subtraktion und Ziffernverständnis")
			.withHerkunftstyp(RaetselHerkunftTyp.ZITAT)
			.withQuelle(quelle);

		return editRaetselPayload;

	}
}
