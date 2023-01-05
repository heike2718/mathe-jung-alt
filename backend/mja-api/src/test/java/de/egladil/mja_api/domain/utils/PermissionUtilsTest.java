// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.web.mja_auth.session.AuthenticatedUser;

/**
 * PermissionUtilsTest
 */
public class PermissionUtilsTest {

	@Nested
	class DeprecatedMethodsTests {
		@Test
		void should_hasUserPermissionToChangeReturnTrue_when_adminAndOwner() {

			// Arrange
			String ownerId = "hallo";

			// Act + Assert
			assertTrue(PermissionUtils.hasUserPermissionToChange(ownerId, ownerId, true));

		}

		@Test
		void should_hasUserPermissionToChangeReturnTrue_when_adminAndNotOwner() {

			// Arrange
			String ownerId = "hallo";

			// Act + Assert
			assertTrue(PermissionUtils.hasUserPermissionToChange("du-da", ownerId, true));

		}

		@Test
		void should_hasUserPermissionToChangeReturnTrue_when_notAdminButOwner() {

			// Arrange
			String ownerId = "hallo";

			// Act + Assert
			assertTrue(PermissionUtils.hasUserPermissionToChange(ownerId, ownerId, false));
		}

		@Test
		void should_hasUserPermissionToChangeReturnFalse_when_notAdminAndNotOwner() {

			// Arrange
			String ownerId = "hallo";

			// Act + Assert
			assertFalse(PermissionUtils.hasUserPermissionToChange("du-da", ownerId, false));
		}
	}

	@Nested
	class ReadPermissionTests {

		@Test
		void should_hasReadPermissionReturTrue_when_userNullAndFreigegeben() {

			// Arrange
			AuthenticatedUser user = null;

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasReadPermissionReturFalse_when_userNullAndNichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = null;

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.ERFASST);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userStandardAndFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userStandardAndNichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.ERFASST);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAutorAndFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAutorAndNichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.ERFASST);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAdminAndFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAdminAndNichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasReadPermission(user, DomainEntityStatus.ERFASST);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_restrictSucheToFreigegebenReturnFalse_when_userIsAutor() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act + Assert
			assertFalse(PermissionUtils.restrictSucheToFreigegeben(user));
		}

		@Test
		void should_restrictSucheToFreigegebenReturnFalse_when_userIsAdmin() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act + Assert
			assertFalse(PermissionUtils.restrictSucheToFreigegeben(user));
		}

		@Test
		void should_restrictSucheToFreigegebenReturnTrue_when_userIsStandard() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "PRIVAT", "STANDARD" }, null, "bladi");

			// Act + Assert
			assertTrue(PermissionUtils.restrictSucheToFreigegeben(user));
		}

		@Test
		void should_restrictSucheToFreigegebenReturnTrue_when_userIsNull() {

			// Act + Assert
			assertTrue(PermissionUtils.restrictSucheToFreigegeben(null));
		}

	}

	@Nested
	class WritePermissionTests {

		@Test
		void should_hasWritePermissionReturnFalse_when_userNullAndOwnerIdNull() {

			// Arrange
			AuthenticatedUser user = null;

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, null);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userNullAndOwnerIdNotNull() {

			// Arrange
			AuthenticatedUser user = null;

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, "");

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userStandardAndOwnerIdNull() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, null);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userStandardAndOwnerIdNotNull() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, "");

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userAutorAndOwnerIdNull() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, null);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userAutorAndOwnerIdNotUserId() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, "blubb");

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAutorAndOwnerIdEqualsUserId() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, "bladi");

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAdminAndOwnerIdNull() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, null);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAdminAndOwnerIdNotUserId() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, "blubb");

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAdminAndOwnerIdEqualsUserId() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.hasWritePermission(user, "bladi");

			// Assert
			assertTrue(result);
		}
	}

	@Nested
	class AdminTests {

		@Test
		void should_isUserAdminReturnFalse_when_userNull() {

			// Arrange
			AuthenticatedUser user = null;

			// Act
			boolean result = PermissionUtils.isUserAdmin(user);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAdminReturnTrue_when_userNotNullAndAdmin() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.isUserAdmin(user);

			// Assert
			assertTrue(result);

		}

		@Test
		void should_isUserAdminReturnFalse_when_userNotNullAndAutor() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.isUserAdmin(user);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAdminReturnFalse_when_userNotNullAndStandard() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.isUserAdmin(user);

			// Assert
			assertFalse(result);

		}

	}

	@Nested
	class AutorTests {

		@Test
		void should_isUserAutorReturnFalse_when_userNull() {

			// Arrange
			AuthenticatedUser user = null;

			// Act
			boolean result = PermissionUtils.isUserAutor(user);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAutorReturnTrue_when_userNotNullAndAutor() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "AUTOR", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.isUserAutor(user);

			// Assert
			assertTrue(result);

		}

		@Test
		void should_isUserAutorReturnFalse_when_userNotNullAndAdmin() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "ADMIN", "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.isUserAutor(user);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAutorReturnFalse_when_userNotNullAndStandard() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser("hallo", new String[] { "STANDARD" }, null, "bladi");

			// Act
			boolean result = PermissionUtils.isUserAutor(user);

			// Assert
			assertFalse(result);

		}

	}
}
