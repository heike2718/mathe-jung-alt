// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * FullDatabaseTestProfile
 */
public class FullDatabaseTestProfile implements QuarkusTestProfile {

	@Override
	public String getConfigProfile() {

		// aus application.properties
		return "full-db-test";
	}

}
