// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

/**
 * RaetselPermissionDelegateTest
 */
@QuarkusTest
public class RaetselPermissionDelegateTest {

	private static final String OWNER = "e729a55a-7a48-4fc5-b5b3-542d5c42d335";

	private static final String USER_ID = "3ad560c3-372e-4166-8bea-6e4c6272d0fd";

	private static final String RAETSEL_ID = "173dc52b-4dca-4022-8278-f7ad0b0f5377";

	private static final String SCHLUESSEL = "01234";

	@InjectMock
	AuthenticationContext authCtx;

	@Inject
	RaetselPermissionDelegate delegate;

	@Nested
	class WritePermissionTestsANONYM {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ANONYM);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = OWNER;
			ausDB.uuid = RAETSEL_ID;

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
		void should_checkPermisionThrow403_when_UserIsOWNER() {

			// das ist eine theoretische Möglichkeit, weil Standarduser keine Rätsel besitzen können

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ANONYM);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = USER_ID;
			ausDB.uuid = RAETSEL_ID;

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
	class WritePermissionTestsSTANDARD {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.STANDARD);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = OWNER;
			ausDB.uuid = RAETSEL_ID;

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
		void should_checkPermisionThrow403_when_UserIsOWNER() {

			// das ist eine theoretische Möglichkeit, weil Standarduser keine Rätsel besitzen können

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.STANDARD);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = USER_ID;
			ausDB.uuid = RAETSEL_ID;

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
	class WritePermissionTestsAUTOR {

		@Test
		void should_checkPermisionThrow403_when_UserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.AUTOR);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = OWNER;
			ausDB.uuid = RAETSEL_ID;

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
		void should_checkPermisionNotThrow403_when_UserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.AUTOR);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = USER_ID;
			ausDB.uuid = RAETSEL_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class WritePermissionTestsADMIN {

		@Test
		void should_checkPermisionNotThrow403_when_UserNotOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = OWNER;
			ausDB.uuid = RAETSEL_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_UserIsOWNER() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID).withBenutzerart(Benutzerart.ADMIN);

			PersistentesRaetsel ausDB = new PersistentesRaetsel();
			ausDB.owner = USER_ID;
			ausDB.uuid = RAETSEL_ID;

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkWritePermission(ausDB);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class RaetselDetailsReadPermissionTestsANONYM {

		@Test
		void should_checkPermisionThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ANONYM);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(false);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(raetsel);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}
		}

		@Test
		void should_checkPermisionThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ANONYM);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(true);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(raetsel);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}
		}
	}

	@Nested
	class RaetselDetailsReadPermissionTestsSTANDARD {

		@Test
		void should_checkPermisionThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.STANDARD);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(false);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(raetsel);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}
		}

		@Test
		void should_checkPermisionNotThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.STANDARD);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(true);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class RaetselDetailsReadPermissionTestsAUTOR {

		@Test
		void should_checkPermisionNotThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.AUTOR);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(false);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.AUTOR);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(true);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class RaetselDetailsReadPermissionTestsADMIN {

		@Test
		void should_checkPermisionNotThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ADMIN);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(false);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ADMIN);

			Raetsel raetsel = new Raetsel(RAETSEL_ID).withSchluessel(SCHLUESSEL).withFreigegeben(true);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class RaetselReadPermissionTestsANONYM {

		@Test
		void should_checkPermisionThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ANONYM);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(false).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(raetsel);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}
		}

		@Test
		void should_checkPermisionThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ANONYM);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(true).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(raetsel);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}
		}
	}

	@Nested
	class RaetselReadPermissionTestsSTANDARD {

		@Test
		void should_checkPermisionThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.STANDARD);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(false).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			try {

				delegate.checkReadPermission(raetsel);
				fail("keine WebApplicationException");
			} catch (WebApplicationException e) {

				assertEquals(403, e.getResponse().getStatus());

				verify(authCtx).getUser();
			}
		}

		@Test
		void should_checkPermisionNotThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.STANDARD);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(true).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class RaetselReadPermissionTestsAUTOR {

		@Test
		void should_checkPermisionNotThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.AUTOR);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(false).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.AUTOR);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(true).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class RaetselReadPermissionTestsADMIN {

		@Test
		void should_checkPermisionNotThrow403_when_NichtFreigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ADMIN);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(false).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}

		@Test
		void should_checkPermisionNotThrow403_when_Freigegeben() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ADMIN);

			RaetselsucheTrefferItem raetsel = new RaetselsucheTrefferItem().withFreigegeben(true).withId(RAETSEL_ID)
				.withSchluessel(SCHLUESSEL);

			when(authCtx.getUser()).thenReturn(user);

			// Act
			delegate.checkReadPermission(raetsel);

			// Assert
			verify(authCtx).getUser();
		}
	}

	@Nested
	class FreigegebeneRaetselTests {

		@Test
		void should_isOnlyReadFreigegebeneReturnTrue_when_userANONYM() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ANONYM);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			assertTrue(delegate.isOnlyReadFreigegebene());

		}

		@Test
		void should_isOnlyReadFreigegebeneReturnTrue_when_userSTANDARD() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.STANDARD);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			assertTrue(delegate.isOnlyReadFreigegebene());

		}

		@Test
		void should_isOnlyReadFreigegebeneReturnFalse_when_userAUTOR() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.AUTOR);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			assertFalse(delegate.isOnlyReadFreigegebene());

		}

		@Test
		void should_isOnlyReadFreigegebeneReturnFalse_when_userADMIN() {

			// Arrange
			AuthenticatedUser user = new AuthenticatedUser(USER_ID)
				.withBenutzerart(Benutzerart.ADMIN);

			when(authCtx.getUser()).thenReturn(user);

			// Act + Assert
			assertFalse(delegate.isOnlyReadFreigegebene());

		}

	}
}
