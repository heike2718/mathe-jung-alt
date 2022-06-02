// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.profiles;

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
