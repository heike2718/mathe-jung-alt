// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesMedium;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

/**
 * MedienPermissionDelegateTest
 */
@QuarkusTest
public class MedienPermissionDelegateTest {

	private static final String OWNER = "e729a55a-7a48-4fc5-b5b3-542d5c42d335";

	private static final String USER_ID = "3ad560c3-372e-4166-8bea-6e4c6272d0fd";

	private static final String MEDIUM_ID = "173dc52b-4dca-4022-8278-f7ad0b0f5377";

	@InjectMock
	AuthenticationContext authCtx;

	@Inject
	MedienPermissionDelegate delegate;

	@Nested
	@DisplayName("anonyme User haben keine Leseberechtigung")
	class ReadPermissionTestsAnonymerUser {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

		@Test
		void should_checkPermisionThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

	}

	@Nested
	@DisplayName("Standarduser haben keine Leseberechtigung")
	class ReadPermissionTestsSTANDARD {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

		@Test
		void should_checkPermisionThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

	}

	@Nested
	@DisplayName("Autoren haben nur Leseberechtigung für eigene Medien")
	class ReadPermissionTestsAUTOR {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

		@Test
		void should_checkPermisionThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

	}

	@Nested
	@DisplayName("Admins haben Leseberechtigung für alle Medien")
	class ReadPermissionTestsADMIN {

		@Test
		void should_checkPermisionNotThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

	}

	@Nested
	@DisplayName("anonyme User haben keine Schreibberechtigung")
	class WritePermissionTestsAnonymerUser {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkWritePermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

		@Test
		void should_checkPermisionThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkWritePermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

	}

	@Nested
	@DisplayName("Standarduser haben keine Schreibberechtigung")
	class WritePermissionTestsSTANDARD {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkWritePermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

		@Test
		void should_checkPermisionThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkWritePermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

	}

	@Nested
	@DisplayName("Autoren haben nur für eigene Medien Schreibberechtigung")
	class WritePermissionTestsAUTOR {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkWritePermission(ausDB);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}

		}

		@Test
		void should_checkPermisionNotThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

	}

	@Nested
	@DisplayName("Admins haben immer Schreibberechtigung")
	class WritePermissionTestsADMIN {

		@Test
		void should_checkPermisionNotThrow403_when_UserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = OWNER;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();

		}

		@Test
		void should_checkPermisionNotThrow403_when_UserOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User sehen keine Medien

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistentesMedium ausDB = new PersistentesMedium();
			ausDB.owner = USER_ID;
			ausDB.uuid = MEDIUM_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

	}

}
