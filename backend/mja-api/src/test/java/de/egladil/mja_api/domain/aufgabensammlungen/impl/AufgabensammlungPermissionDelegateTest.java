// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.aufgabensammlungen.impl;

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
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

/**
 * AufgabensammlungPermissionDelegateTest
 */
@QuarkusTest
public class AufgabensammlungPermissionDelegateTest {

	private static final String OWNER = "e729a55a-7a48-4fc5-b5b3-542d5c42d335";

	private static final String USER_ID = "3ad560c3-372e-4166-8bea-6e4c6272d0fd";

	private static final String AUFGABENSAMMLUNG_ID = "173dc52b-4dca-4022-8278-f7ad0b0f5377";

	@InjectMock
	AuthenticationContext authCtx;

	@Inject
	AufgabensammlungPermissionDelegate delegate;

	@Nested
	@DisplayName("Anonyme User haben keine Schreibberechtigung auf Aufgabensammlungen")
	class WritePermissionTestsAnonymerUser {

		@Test
		void should_checkPermisionThrow403_when_PrivatUserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

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
		void should_checkPermisionThrow403_when_privatAndUserIsOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

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
		void should_checkPermisionThrow403_when_publicAndUserIsOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

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
		void should_checkPermisionThrow403_when_publicAndUserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

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
	@DisplayName("Standarduser haben Schreibberechtigung nur dann, wenn sie OWNER sind")
	class WritePermissionTestsStandarduser {

		@Test
		void should_checkPermisionThrow403_when_privatAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

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
		void should_checkPermisionNotThrow403_when_privatAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserIsOWNER() {

			// das ist ein theoretischer Fall, denn STANDAR-User können keine public Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionThrow403_when_publicAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

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
	@DisplayName("Autoren haben Schreibberechtigung nur dann, wenn sie OWNER sind")
	class WritePermissionTestsAUTOR {

		@Test
		void should_checkPermisionThrow403_when_privatAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

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
		void should_checkPermisionNotThrow403_when_privatAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionThrow403_when_publicAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

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
	@DisplayName("Admins haben Schreibberechtigung für alle Aufgabensammlungen")
	class WritePermissiontestsADMIN {

		@Test
		void should_checkPermisionThrow403_when_privatAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();

		}

		@Test
		void should_checkPermisionNotThrow403_when_privatAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

	}

	// /////////////////////////////////////////////////////

	@Nested
	@DisplayName("Anonyme User haben keine Leseberechtigung auf Aufgabensammlungen")
	class ReadPermissionTestsAnonymerUser {

		@Test
		void should_checkPermisionThrow403_when_PrivatUserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

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
		void should_checkPermisionThrow403_when_privatAndUserIsOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

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
		void should_checkPermisionThrow403_when_publicAndUserIsOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

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
		void should_checkPermisionThrow403_when_publicAndUserNotOWNER() {

			// das ist ein theoretischer Fall, denn anonyme User können keine Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ANONYM);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

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
	@DisplayName("Standarduser haben Leseberechtigung für private Aufgabensammlungen, wenn sie OWNER sind und für alle freigegebenen, nicht privaten Aufgabensammlungen")
	class ReadPermissionTestsStandarduser {

		@Test
		void should_checkPermisionThrow403_when_privatAndNotFreigegebenUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;
			ausDB.freigegeben = false;

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
		void should_checkPermisionNotThrow403_when_privatAndNotFreigegebenAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;
			ausDB.freigegeben = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndFreigegebenAndUserIsOWNER() {

			// das ist ein theoretischer Fall, denn STANDAR-User können keine public Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;
			ausDB.freigegeben = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndFreigegebenAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;
			ausDB.freigegeben = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();

		}

		@Test
		void should_checkPermisionThrow403_when_publicAndNotFreigegebenAndUserIsOWNER() {

			// das ist ein theoretischer Fall, denn STANDAR-User können keine public Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;
			ausDB.freigegeben = false;

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
		void should_checkPermisionThrow403_when_publicAndNotFreigegebenAndUserIsNotOWNER() {

			// das ist ein theoretischer Fall, denn STANDAR-User können keine public Aufgabensammlungen anlegen.

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.STANDARD);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;
			ausDB.freigegeben = false;

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
	@DisplayName("Autoren haben Leseberechtigung für private Aufgabensammlungen, wenn sie OWNER sind und für alle public Aufgabensammlungen")
	class ReadPermissionTestsAUTOR {

		@Test
		void should_checkPermisionThrow403_when_privatAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

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
		void should_checkPermisionNotThrow403_when_privatAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.AUTOR);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();

		}

	}

	@Nested
	@DisplayName("Admins haben Leseberechtigung für alle Aufgabensammlungen")
	class ReadPermissiontestsADMIN {

		@Test
		void should_checkPermisionThrow403_when_privatAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();

		}

		@Test
		void should_checkPermisionNotThrow403_when_privatAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = true;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = USER_ID;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_publicAndUserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistenteAufgabensammlung ausDB = new PersistenteAufgabensammlung();
			ausDB.owner = OWNER;
			ausDB.uuid = AUFGABENSAMMLUNG_ID;
			ausDB.privat = false;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

	}

}
