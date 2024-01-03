// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * FullDatabaseAutorTestProfile
 */
public class FullDatabaseAutorTestProfile implements QuarkusTestProfile {

	@Override
	public String getConfigProfile() {

		// Präfix = _FULL-DB-AUTOR-TEST_ (siehe .env und templates/env-template)
		return "full-db-autor-test";
	}

}
