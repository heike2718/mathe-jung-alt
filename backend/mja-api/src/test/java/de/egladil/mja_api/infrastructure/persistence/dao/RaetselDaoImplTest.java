// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.Suchmodus;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

/**
 * RaetselDaoImplTest
 */
@QuarkusTest
@TestProfile(FullDatabaseTestProfile.class)
public class RaetselDaoImplTest {

	@Inject
	RaetselDaoImpl dao;

	@Test
	void schould_findRaetsel_when_schluesselliste() {

		// Arrange
		List<String> schluessel = Arrays.asList(new String[] { "02623", "02816", "02612", "keiner" });

		// Act
		List<PersistentesRaetsel> trefferliste = dao.findWithSchluessel(schluessel);

		// Assert
		assertEquals(3, trefferliste.size());

	}

	// ================== Volltext-Tests ======================================================

	@Test
	void should_countRaetselVolltextReturnExpected_when_2WorteUndINTERSECTION() {

		// Arrange
		String suchstring = "Kinder Schwester";
		Suchmodus suchmodus = Suchmodus.INTERSECTION;

		// Act
		long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, false);

		// Assert
		assertEquals(1, anzahl);

	}

	@Test
	void should_countRaetselVolltextReturnExpected_when_2WorteUndUNIONUndNurFREIGEGEBEN() {

		// Arrange
		String suchstring = "Kinder Tiere";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, true);

		// Assert
		assertEquals(1, anzahl);

	}

	@Test
	void should_countRaetselVolltextReturnExpected_when_2WorteUndUNION() {

		// Arrange
		String suchstring = "Kinder Tiere";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, false);

		// Assert
		assertEquals(3, anzahl);

	}

	@Test
	void should_findRaetselVolltextReturnExpected_when_2WorteUndINTERSECTION() {

		// Arrange
		String suchstring = "Kinder Schwester";
		Suchmodus suchmodus = Suchmodus.INTERSECTION;

		// Act
		List<PersistentesRaetsel> treffer = dao.findRaetselVolltext(suchstring, suchmodus, 0, 10, SortDirection.asc, false);

		// Assert
		assertEquals(1, treffer.size());

		assertEquals("02621", treffer.get(0).schluessel);

	}

	@Test
	void should_findRaetselVolltextReturnExpected_when_2WorteUndUNIONUndNurFreigegeben() {

		// Arrange
		String suchstring = "Kinder Tiere";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		List<PersistentesRaetsel> treffer = dao.findRaetselVolltext(suchstring, suchmodus, 0, 10, SortDirection.asc, true);

		// Assert
		assertEquals(1, treffer.size());

		assertEquals("02613", treffer.get(0).schluessel);

	}

	@Test
	void should_findRaetselVolltextReturnExpected_when_2WorteUndUNION() {

		// Arrange
		String suchstring = "Kinder Tiere";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		List<PersistentesRaetsel> treffer = dao.findRaetselVolltext(suchstring, suchmodus, 0, 10, SortDirection.asc, false);

		// Assert
		assertEquals(3, treffer.size());

		assertEquals("02613", treffer.get(0).schluessel);
		assertEquals("02621", treffer.get(1).schluessel);
		assertEquals("02818", treffer.get(2).schluessel);

	}

	// ================== Admin-Filter-Tests ======================================================

	@Test
	void should_countRaetselWithFilterReturnExpected_when_2WorteUndINTERSECTION() {

		// Arrange
		String suchstring = "Kinder Schwester";
		String deskriptoren = "1,2";
		Suchmodus suchmodus = Suchmodus.INTERSECTION;

		// Act
		long anzahl = dao.countRaetselWithFilter(suchstring, deskriptoren, suchmodus, false);

		// Assert
		assertEquals(1, anzahl);

	}

	@Test
	void should_countRaetselWithFilterReturnExpected_when_2WorteUndUNION() {

		// Arrange
		String suchstring = "Kinder Tiere";
		String deskriptoren = "1,11";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		long anzahl = dao.countRaetselWithFilter(suchstring, deskriptoren, suchmodus, false);

		// Assert
		assertEquals(2, anzahl);

	}

	@Test
	void should_countRaetselWithFilterReturnExpected_when_2WorteUndUNIONNurFreigegeben() {

		// Arrange
		String suchstring = "Kinder Tiere";
		String deskriptoren = "1,11";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		long anzahl = dao.countRaetselWithFilter(suchstring, deskriptoren, suchmodus, true);

		// Assert
		assertEquals(1, anzahl);

	}

	@Test
	void should_findRaetselWithFilterReturnExpected_when_2WorteUndINTERSECTION() {

		// Arrange
		String suchstring = "Kinder Schwester";
		String deskriptoren = "1,2";
		Suchmodus suchmodus = Suchmodus.INTERSECTION;

		// Act
		List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchstring, deskriptoren, suchmodus, 0, 10, SortDirection.asc,
			false);

		// Assert
		assertEquals(1, treffer.size());

		assertEquals("02621", treffer.get(0).schluessel);

	}

	@Test
	void should_findRaetselWithFilterReturnExpected_when_2WorteUndUNION() {

		// Arrange
		String suchstring = "Kinder Tiere";
		String deskriptoren = "1,2";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchstring, deskriptoren, suchmodus, 0, 10, SortDirection.asc,
			false);

		// Assert
		assertEquals(3, treffer.size());

		assertEquals("02613", treffer.get(0).schluessel);
		assertEquals("02621", treffer.get(1).schluessel);
		assertEquals("02818", treffer.get(2).schluessel);

	}

	@Test
	void should_findRaetselWithFilterReturnExpected_when_2WorteUndUNIONUndNurFREIGEGEBEN() {

		// Arrange
		String suchstring = "Kinder Tiere";
		String deskriptoren = "1,2";
		Suchmodus suchmodus = Suchmodus.UNION;

		// Act
		List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchstring, deskriptoren, suchmodus, 0, 10, SortDirection.asc,
			true);

		// Assert
		assertEquals(1, treffer.size());

		assertEquals("02613", treffer.get(0).schluessel);

	}
}
