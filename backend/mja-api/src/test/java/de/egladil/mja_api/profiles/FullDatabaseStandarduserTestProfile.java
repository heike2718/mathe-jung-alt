// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * FullDatabaseStandarduserTestProfile
 */
public class FullDatabaseStandarduserTestProfile implements QuarkusTestProfile {

	@Override
	public String getConfigProfile() {

		// Präfix = _FULL-DB-STANDARD-TEST_ (siehe .env und templates/env-template)
		return "full-db-standard-test";
	}

}
