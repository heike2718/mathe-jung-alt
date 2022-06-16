// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.profiles;

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
