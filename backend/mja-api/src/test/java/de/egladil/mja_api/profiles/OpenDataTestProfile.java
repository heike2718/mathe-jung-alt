// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * OpenDataTestProfile
 */
public class OpenDataTestProfile implements QuarkusTestProfile {

	@Override
	public String getConfigProfile() {

		// aus application.properties
		return "open-data-test";
	}

}
