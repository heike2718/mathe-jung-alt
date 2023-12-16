// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * FullDatabaseAdminTestProfile
 */
public class FullDatabaseAdminTestProfile implements QuarkusTestProfile {

	@Override
	public String getConfigProfile() {

		// Präfix = _FULL-DB-ADMIN-TEST_ (siehe .env und templates/env-template)
		return "full-db-admin-test";
	}

}
