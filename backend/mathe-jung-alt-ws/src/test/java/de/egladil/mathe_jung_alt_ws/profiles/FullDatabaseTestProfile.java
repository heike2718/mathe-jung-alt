// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.profiles;

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
