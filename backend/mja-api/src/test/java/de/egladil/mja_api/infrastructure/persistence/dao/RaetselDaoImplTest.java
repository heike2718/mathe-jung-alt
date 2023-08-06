// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.profiles.FullDatabaseTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

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

}
