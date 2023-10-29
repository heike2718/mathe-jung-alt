// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.DomainEntityStatus;
import io.quarkus.test.junit.QuarkusTest;

/**
 * PermissionUtilsTest
 */
@QuarkusTest
public class PermissionUtilsTest {

	@Nested
	class ReadPermissionTests {

		@Test
		void should_hasReadPermissionReturTrue_when_userNullAndFreigegeben() {

			// Arrange
			List<String> roles = null;

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasReadPermissionReturFalse_when_userNullAndNichtFreigegeben() {

			// Arrange
			List<String> roles = null;

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.ERFASST);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userStandardAndFreigegeben() {

			// Arrange
			List<String> roles = new ArrayList<>();

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userStandardAndNichtFreigegeben() {

			// Arrange
			List<String> roles = new ArrayList<>();

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.ERFASST);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAutorAndFreigegeben() {

			// Arrange
			List<String> roles = Collections.singletonList("AUTOR");

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAutorAndNichtFreigegeben() {

			// Arrange
			List<String> roles = Collections.singletonList("AUTOR");

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.ERFASST);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAdminAndFreigegeben() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.FREIGEGEBEN);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasReadPermissionReturTrue_when_userAdminAndNichtFreigegeben() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act
			boolean result = PermissionUtils.hasReadPermission(roles, DomainEntityStatus.ERFASST);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_restrictSucheToFreigegebenReturnFalse_when_userIsAutor() {

			// Arrange
			List<String> roles = Collections.singletonList("AUTOR");

			// Act + Assert
			assertFalse(PermissionUtils.restrictSucheToFreigegeben(roles));
		}

		@Test
		void should_restrictSucheToFreigegebenReturnFalse_when_userIsAdmin() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act + Assert
			assertFalse(PermissionUtils.restrictSucheToFreigegeben(roles));
		}

		@Test
		void should_restrictSucheToFreigegebenReturnTrue_when_userIsStandard() {

			// Arrange
			List<String> roles = new ArrayList<>();

			// Act + Assert
			assertTrue(PermissionUtils.restrictSucheToFreigegeben(roles));
		}

		@Test
		void should_restrictSucheToFreigegebenReturnTrue_when_userIsNull() {

			List<String> roles = null;
			// Act + Assert
			assertTrue(PermissionUtils.restrictSucheToFreigegeben(roles));
		}

	}

	@Nested
	class WritePermissionTests {

		@Test
		void should_hasWritePermissionReturnFalse_when_userNullAndOwnerIdNull() {

			// Arrange
			List<String> roles = null;

			// Act
			boolean result = PermissionUtils.hasWritePermission(null, roles, null);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userNullAndOwnerIdNotNull() {

			// Arrange
			List<String> roles = null;

			// Act
			boolean result = PermissionUtils.hasWritePermission(null, roles, "blubb");

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userStandardAndOwnerIdNull() {

			// Arrange
			List<String> roles = new ArrayList<>();

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, null);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userStandardAndOwnerIdNotNull() {

			// Arrange
			List<String> roles = new ArrayList<>();

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, "blubb");

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userAutorAndOwnerIdNull() {

			// Arrange
			List<String> roles = Arrays.asList(new String[] { "AUTOR" });

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, null);

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnFalse_when_userAutorButNotOwner() {

			// Arrange
			List<String> roles = Arrays.asList(new String[] { "AUTOR" });

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, "blubb");

			// Assert
			assertFalse(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAutorAndOwner() {

			// Arrange
			List<String> roles = Arrays.asList(new String[] { "AUTOR" });

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, "ggf");

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAdminAndOwnerIdNull() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, null);

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAdminAndOwnerIdNotUserId() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, "blubb");

			// Assert
			assertTrue(result);
		}

		@Test
		void should_hasWritePermissionReturnTrue_when_userAdminAndOwnerIdEqualsUserId() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act
			boolean result = PermissionUtils.hasWritePermission("ggf", roles, "ggf");

			// Assert
			assertTrue(result);
		}
	}

	@Nested
	class AdminTests {

		@Test
		void should_isUserAdminReturnFalse_when_userNull() {

			// Arrange
			List<String> roles = null;

			// Act
			boolean result = PermissionUtils.isUserAdmin(roles);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAdminReturnTrue_when_userNotNullAndAdmin() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act
			boolean result = PermissionUtils.isUserAdmin(roles);

			// Assert
			assertTrue(result);

		}

		@Test
		void should_isUserAdminReturnFalse_when_userNotNullAndAutor() {

			// Arrange
			List<String> roles = Collections.singletonList("AUTOR");

			// Act
			boolean result = PermissionUtils.isUserAdmin(roles);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAdminReturnFalse_when_userNotNullAndStandard() {

			// Arrange
			List<String> roles = new ArrayList<>();

			// Act
			boolean result = PermissionUtils.isUserAdmin(roles);

			// Assert
			assertFalse(result);

		}

	}

	@Nested
	class AutorTests {

		@Test
		void should_isUserAutorReturnFalse_when_userNull() {

			// Arrange
			List<String> roles = null;

			// Act
			boolean result = PermissionUtils.isUserAutor(roles);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAutorReturnTrue_when_userNotNullAndAutor() {

			// Arrange
			List<String> roles = Collections.singletonList("AUTOR");

			// Act
			boolean result = PermissionUtils.isUserAutor(roles);

			// Assert
			assertTrue(result);

		}

		@Test
		void should_isUserAutorReturnFalse_when_userNotNullAndAdmin() {

			// Arrange
			List<String> roles = Collections.singletonList("ADMIN");

			// Act
			boolean result = PermissionUtils.isUserAutor(roles);

			// Assert
			assertFalse(result);

		}

		@Test
		void should_isUserAutorReturnFalse_when_userNotNullAndStandard() {

			// Arrange
			List<String> roles = new ArrayList<>();

			// Act
			boolean result = PermissionUtils.isUserAutor(roles);

			// Assert
			assertFalse(result);

		}

	}
}
