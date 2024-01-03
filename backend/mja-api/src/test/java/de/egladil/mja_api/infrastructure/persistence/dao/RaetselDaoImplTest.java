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
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.profiles.FullDatabaseAdminTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;

/**
 * RaetselDaoImplTest
 */
@QuarkusTest
@TestProfile(FullDatabaseAdminTestProfile.class)
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
	class VolltextsucheTests {

		@Test
		void should_countRaetselVolltextReturnExpected_when_INTERSECTION() {

			// Arrange
			String suchstring = "Kinder Schwester";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;

			// Act
			long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, false);

			// Assert
			assertEquals(1, anzahl);

		}

		@Test
		void should_countRaetselVolltextReturnExpected_when_UNIONUndNurFREIGEGEBEN() {

			// Arrange
			String suchstring = "Kinder Tiere";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

			// Act
			long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, true);

			// Assert
			assertEquals(1, anzahl);

		}

		@Test
		void should_countRaetselVolltextReturnExpected_when_UNION() {

			// Arrange
			String suchstring = "Kinder Tiere";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;

			// Act
			long anzahl = dao.countRaetselVolltext(suchstring, suchmodus, false);

			// Assert
			assertEquals(3, anzahl);

		}

		@Test
		void should_findRaetselVolltextReturnExpected_when_INTERSECTION() {

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
		void should_findRaetselVolltextReturnExpected_when_UNIONUndNurFreigegeben() {

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
		void should_findRaetselVolltextReturnExpected_when_UNION() {

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
			assertEquals(PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusLikeFreigegebeneDesc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.LIKE;
			SortDirection sortDirection = SortDirection.desc;
			boolean nurFreigegebene = true;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_DESKRIPTOREN_DESC,
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
			assertEquals(PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

		@Test
		void suchmodusNotLikeFreigegebeneDesc() {

			// Arrange
			SuchmodusDeskriptoren suchmodus = SuchmodusDeskriptoren.NOT_LIKE;
			SortDirection sortDirection = SortDirection.desc;
			boolean nurFreigegebene = true;

			// Assert
			assertEquals(PersistentesRaetsel.FIND_WITH_FREIGEGEBEN_AND_NOT_WITH_DESKRIPTOREN_DESC,
				dao.queryNameFilteredSearch(suchmodus, sortDirection, nurFreigegebene));

		}

	}

	@Nested
	class FilteredSucheTests {

		private String suchstring = "Kinder Tiere Kreis Monat Spielzeug Kekse Känguru";

		private String deskriptoren = "8,11";

		@Test
		void should_countRaetselWithFilterReturnExpected_when_AlleINTERSECTIONUndDeskriptorenLIKE() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "8";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter, nurFregegebene);

			// Assert
			assertEquals(3, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleINTERSECTIONUndDeskriptorenLIKE() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "8";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(3, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleINTERSECTIONUndDeskriptorenLIKEDesc() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "8";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(3, treffer.size());

			assertEquals("02800", treffer.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithFilterReturnExpected_when_INTERSECTIONUndDeskriptorenLIKE_FREIGEGEBEN() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "8";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter, nurFregegebene);

			// Assert
			assertEquals(1, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_INTERSECTIONUndDeskriptorenLIKE_FREIGEGEBEN() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "8";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(1, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_INTERSECTIONUndDeskriptorenLIKE_FREIGEGEBEN_Desc() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "8";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(1, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithFilterReturnExpected_when_AlleUNIONUndDeskriptorenLIKE() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter,
				nurFregegebene);

			// Assert
			assertEquals(8, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleUNIONAndDeskriptorenLIKE() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				8,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(8, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleUNIONAndDeskriptorenLIKEDesc() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);
			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				8,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(8, treffer.size());

			assertEquals("02816", treffer.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithFilterReturnExpected_when_UNIONUndDeskriptorenLIKEUndFREIGEGEBEN() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter,
				nurFregegebene);

			// Assert
			assertEquals(3, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_UNIONUndDeskriptorenLIKEUndFREIGEGEBEN() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(3, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_UNIONUndDeskriptorenLIKEUndFREIGEGEBENDesc() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(3, treffer.size());

			assertEquals("02613", treffer.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithFilterReturnExpected_when_AlleINTERSECTIONUndDeskriptorenNOT_LIKE() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "6";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter, nurFregegebene);

			// Assert
			assertEquals(3, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleINTERSECTIONUndDeskriptorenNOT_LIKE() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "6";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(3, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleINTERSECTIONUndDeskriptorenNOT_LIKEDesc() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "6";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(3, treffer.size());

			assertEquals("02800", treffer.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithFilterReturnExpected_when_INTERSECTIONUndFREIGEGEBENUndnDeskriptorenNOT_LIKE() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "6";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter, nurFregegebene);

			// Assert
			assertEquals(1, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_INTERSECTIONUndFREIGEGEBENUndDeskriptorenNOT_LIKE() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "6";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(1, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleINTERSECTIONUndFREIGEGEBENUndDeskriptorenNOT_LIKEDesc() {

			// Arrange
			suchstring = "zahl känguru";
			deskriptoren = "6";
			SuchmodusVolltext suchmodus = SuchmodusVolltext.INTERSECTION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				10,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(1, treffer.size());

			assertEquals("02604", treffer.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithFilterReturnExpected_when_AlleUNIONUndDeskriptorenNOT_LIKE() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter,
				nurFregegebene);

			// Assert
			assertEquals(16, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleUNIONAndDeskriptorenNOT_LIKE() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				20,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(16, treffer.size());

			assertEquals("02516", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleUNIONAndDeskriptorenNOT_LIKEDesc() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = false;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);
			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				20,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(16, treffer.size());

			assertEquals("02818", treffer.get(0).schluessel);

		}

		@Test
		void should_countRaetselWithFilterReturnExpected_when_UNIONUndDeskriptorenNOT_LIKEUndFREIGEGEBEN() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			long anzahl = dao.countRaetselWithFilter(suchfilter,
				nurFregegebene);

			// Assert
			assertEquals(8, anzahl);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleUNIONAndDeskriptorenNOT_LIKEUndFREIGEGEBEN() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);

			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				20,
				SortDirection.asc, nurFregegebene);

			// Assert
			assertEquals(8, treffer.size());

			assertEquals("02516", treffer.get(0).schluessel);

		}

		@Test
		void should_findRaetselWithFilterReturnExpected_when_AlleUNIONAndDeskriptorenNOT_LIKEUndFREIGEGEBENDesc() {

			// Arrange
			SuchmodusVolltext suchmodus = SuchmodusVolltext.UNION;
			SuchmodusDeskriptoren suchmodusDeskriptoren = SuchmodusDeskriptoren.NOT_LIKE;
			boolean nurFregegebene = true;

			Suchfilter suchfilter = new Suchfilter(suchstring, deskriptoren);
			suchfilter.setModusDeskriptoren(suchmodusDeskriptoren);
			suchfilter.setModusVolltext(suchmodus);
			// Act
			List<PersistentesRaetsel> treffer = dao.findRaetselWithFilter(suchfilter, 0,
				20,
				SortDirection.desc, nurFregegebene);

			// Assert
			assertEquals(8, treffer.size());

			assertEquals("02632", treffer.get(0).schluessel);

		}

	}
}
