// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * AuthorizationUtilsTest
 */
public class AuthorizationUtilsTest {

	@Test
	void should_hasUserPermissionToChangeReturnTrue_when_adminAndOwner() {

		// Arrange
		String ownerId = "hallo";

		// Act + Assert
		assertTrue(AuthorizationUtils.hasUserPermissionToChange(ownerId, ownerId, true));

	}

	@Test
	void should_hasUserPermissionToChangeReturnTrue_when_adminAndNotOwner() {

		// Arrange
		String ownerId = "hallo";

		// Act + Assert
		assertTrue(AuthorizationUtils.hasUserPermissionToChange("du-da", ownerId, true));

	}

	@Test
	void should_hasUserPermissionToChangeReturnTrue_when_notAdminButOwner() {

		// Arrange
		String ownerId = "hallo";

		// Act + Assert
		assertTrue(AuthorizationUtils.hasUserPermissionToChange(ownerId, ownerId, false));
	}

	@Test
	void should_hasUserPermissionToChangeReturnFalse_when_notAdminAndNotOwner() {

		// Arrange
		String ownerId = "hallo";

		// Act + Assert
		assertFalse(AuthorizationUtils.hasUserPermissionToChange("du-da", ownerId, false));
	}

}
