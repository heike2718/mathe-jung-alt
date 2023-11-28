// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.SuchmodusDeskriptoren;
import de.egladil.mja_api.domain.SuchmodusVolltext;
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
	RaetselDao dao;

	@Nested
	class FindWithSchluesselTests {

		@Test
		void schould_findRaetsel_when_schluesselliste() {

			// Arrange
			List<String> schluessel = Arrays.asList(new String[] { "02623", "02816", "02612", "keiner" });

			// Act
			List<PersistentesRaetsel> trefferliste = dao.findWithSchluesselListe(schluessel);

			// Assert
			assertEquals(3, trefferliste.size());

		}
	}

	@Nested
	class SucheVolltextTests {

		@Test
		void should_countRaetselVolltextReturnExpected_when_2WorteUndINTERSECTION() {

			// Arrange
			String suchstring = "Kinder Schwester";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;

			// Act
			long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, false);

			// Assert
			assertEquals(1, anzahl);

		}

		@Test
		void should_countRaetselVolltextReturnExpected_when_2WorteUndUNIONUndNurFREIGEGEBEN() {

			// Arrange
			String suchstring = "Kinder Tiere";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

			// Act
			long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, true);

			// Assert
			assertEquals(1, anzahl);

		}

		@Test
		void should_countRaetselVolltextReturnExpected_when_2WorteUndUNION() {

			// Arrange
			String suchstring = "Kinder Tiere";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

			// Act
			long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, false);

			// Assert
			assertEquals(3, anzahl);

		}

		@Test
		void should_findRaetselVolltextReturnExpected_when_2WorteUndINTERSECTION() {

			// Arrange
			String suchstring = "Kinder Schwester";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;

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
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

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
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselVolltext(suchstring, suchmodus, 0, 10, SortDirection.asc, false);

			// Assert
			assertEquals(3, treffer.size());

			assertEquals("02613", treffer.get(0).schluessel);
			assertEquals("02621", treffer.get(1).schluessel);
			assertEquals("02818", treffer.get(2).schluessel);

		}

	}

	@Nested
	class DeskriptorenSucheTests {

		@Test
		void should_countRaetselWithDeskriptorenReturnExpected_when_likeAndAll() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFreigegeben = false;

			// Act
			long anzahl = dao.countWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, nurFreigegeben);

			// Assert
			assertEquals(13, anzahl);
		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_likeAndAll() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFreigegeben = false;

			// Act
			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 20, 0,
				SortDirection.asc, nurFreigegeben);

			// Assert
			assertFalse(trefferliste.isEmpty());
			assertEquals("02540", trefferliste.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_likeAndAllDesc() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFreigegeben = false;

			// Act
			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 20, 0,
				SortDirection.desc, nurFreigegeben);

			// Assert
			assertFalse(trefferliste.isEmpty());
			assertEquals("02816", trefferliste.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithDeskriptorenReturnExpected_when_likeAndFREIGEGEBEN() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFreigegeben = true;

			// Act
			long anzahl = dao.countWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, nurFreigegeben);

			// Assert
			assertEquals(6, anzahl);
		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_likeAndFREIGEGEBEN() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFreigegeben = true;

			// Act
			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 20, 0,
				SortDirection.asc, nurFreigegeben);

			// Assert
			assertFalse(trefferliste.isEmpty());
			assertEquals("02604", trefferliste.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_likeAndFREIGEGEBENDesc() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFreigegeben = true;

			// Act
			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 20, 0,
				SortDirection.desc, nurFreigegeben);

			// Assert
			assertFalse(trefferliste.isEmpty());
			assertEquals("02640", trefferliste.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithDeskriptorenReturnExpected_when_notLikeAndAll() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFreigegeben = false;

			// Act
			long anzahl = dao.countWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, nurFreigegeben);

			// Assert
			// da währen des gesamten Tests auch neue Rätsel generiert werden, kann man auf Basis der Ausgangsdaten nur eine untere
			// Schranke erwarten.
			assertTrue(anzahl >= 50);
		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_notLikeAndAll() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFreigegeben = false;

			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 50, 0,
				SortDirection.asc, nurFreigegeben);

			// Assert
			assertEquals(50, trefferliste.size());
			assertEquals("00000", trefferliste.get(0).schluessel);
		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_notLikeAndAllDesc() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFreigegeben = false;

			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 50, 0,
				SortDirection.desc, nurFreigegeben);

			// Assert
			assertEquals(50, trefferliste.size());
			assertEquals("99999", trefferliste.get(0).schluessel);
		}

		@Test
		void should_countRaetselWithDeskriptorenReturnExpected_when_notLikeAndFREIGEGEBEN() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFreigegeben = true;

			// Act
			long anzahl = dao.countWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, nurFreigegeben);

			// Assert
			// da währen des gesamten Tests auch neue Rätsel generiert werden, kann man auf Basis der Ausgangsdaten nur eine untere
			// Schranke erwarten.
			assertTrue(anzahl >= 25);
		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_notLikeAndFREIGEGEBEN() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFreigegeben = true;

			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 25, 0,
				SortDirection.asc, nurFreigegeben);

			// Assert
			assertEquals(25, trefferliste.size());
			assertEquals("01219", trefferliste.get(0).schluessel);
		}

		@Test
		void should_findRaetselWithDeskriptorenReturnExpected_when_notLikeAndFREIGEGEBENDesc() {

			// Arrange
			String deskriptoren = "8,11";
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFreigegeben = true;

			List<PersistentesRaetsel> trefferliste = dao.findWithDeskriptoren(deskriptoren, suchmodusDeskriptoren, 25, 0,
				SortDirection.desc, nurFreigegeben);

			// Assert
			assertEquals(25, trefferliste.size());
			assertEquals("02641", trefferliste.get(0).schluessel);
		}
	}

	@Nested
	class SelectCorrectQueryTests {

		@Test
		void suchmodusLikeAllAsc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.LIKE;
			SortDirection sortDirection = SortDirection.asc;
			boolean nurFreigegebene = false;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_DESKRIPTOREN,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusLikeAllDesc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.LIKE;
			SortDirection sortDirection = SortDirection.desc;
			boolean nurFreigegebene = false;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_DESKRIPTOREN_DESC,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusLikeFreigegebeneAsc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.LIKE;
			SortDirection sortDirection = SortDirection.asc;
			boolean nurFreigegebene = true;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_STATUS_AND_DESKRIPTOREN,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusLikeFreigegebeneDesc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.LIKE;
			SortDirection sortDirection = SortDirection.desc;
			boolean nurFreigegebene = true;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_STATUS_AND_DESKRIPTOREN_DESC,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		// ////
		@Test
		void suchmodusNotLikeAllAsc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.NOT_LIKE;
			SortDirection sortDirection = SortDirection.asc;
			boolean nurFreigegebene = false;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusNotLikeAllDesc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.NOT_LIKE;
			SortDirection sortDirection = SortDirection.desc;
			boolean nurFreigegebene = false;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_NOT_WITH_DESKRIPTOREN_DESC,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusNotLikeFreigegebeneAsc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.NOT_LIKE;
			SortDirection sortDirection = SortDirection.asc;
			boolean nurFreigegebene = true;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_STATUS_AND_NOT_WITH_DESKRIPTOREN,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusNotLikeFreigegebeneDesc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.NOT_LIKE;
			SortDirection sortDirection = SortDirection.desc;
			boolean nurFreigegebene = true;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_STATUS_AND_NOT_WITH_DESKRIPTOREN_DESC,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

	}

	@Nested
	class FilteredSucheTests {

		@Test
		void should_countRaetselWithFilterReturnExpected_when_2WorteUndINTERSECTION() {

			// Arrange
			String suchstring = "Kinder Schwester";
			String deskriptoren = "1,2";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;

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
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

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
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

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
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchstring, deskriptoren, suchmodus, 0, 10,
				SortDirection.asc,
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
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchstring, deskriptoren, suchmodus, 0, 10,
				SortDirection.asc,
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
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchstring, deskriptoren, suchmodus, 0, 10,
				SortDirection.asc,
				true);

			// Assert
			assertEquals(1, treffer.size());

			assertEquals("02613", treffer.get(0).schluessel);

		}
	}
}
