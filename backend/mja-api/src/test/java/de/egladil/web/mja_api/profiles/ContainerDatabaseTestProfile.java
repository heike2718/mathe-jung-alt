// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_api.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * ContainerDatabaseTestProfile
 */
public class ContainerDatabaseTestProfile implements QuarkusTestProfile {

	@Override
	public String getConfigProfile() {

		return "test";
	}

}
