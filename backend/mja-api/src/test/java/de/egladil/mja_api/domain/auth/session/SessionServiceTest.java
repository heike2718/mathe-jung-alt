// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.exceptions.AuthException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * SessionServiceTest
 */
@QuarkusTest
public class SessionServiceTest {

	private static final String VALID_JWT_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiODY1ZmM3NS0xYmNmLTQwYzctOTZjMy0zMzc0NDgyNmU0OWYiLCJmdWxsX25hbWUiOiJIZWlrZSBXaW5rZWx2b8OfIiwiaXNzIjoiaGVpa2UyNzE4L2F1dGhwcm92aWRlciIsImdyb3VwcyI6WyJBRE1JTiIsIkFVVE9SIiwiU1RBTkRBUkQiXSwiZXhwIjozMjQ4NjcyODI0LCJpYXQiOjE2OTM0NjkyMjQsImVtYWlsIjoibmV1ZXMtM0BlZ2xhZGlsLmRlIn0.Ml03UBtgBgWOkuEnJ5smqTAya0Is3frr1zHpyUbyFBUbN-3iFqCEjmSCd76hJ9oNzju0VildvsgaCpG9ZABWhEmWLM-lM5cEXXCAqPGfXjf-TEhusS-oiEj45xDyN8RC0V-Fg73NTYaivcOOZgartTToKAabu2eXUjYwBhj649ilVmV9cXHtiStoJcxIm1YfxT9WlKUgnvEybw6g_RF8K9vzIUEFiBllCY07SuD6isQVly_7bNm1vIpmOhg9gO6gJFd9TjJIogaZs7ogZqqu3Q3OzBwsksMx8WEMdH_ERVrK6R3IOIqbdb2Yo8SnUBqHzGNEaPM6I_Vn5EWTK6nIMg";

	private static final String EXPIRED_JWT_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiODY1ZmM3NS0xYmNmLTQwYzctOTZjMy0zMzc0NDgyNmU0OWYiLCJmdWxsX25hbWUiOiJIZWlrZSBXaW5rZWx2b8OfIiwiaXNzIjoiaGVpa2UyNzE4L2F1dGhwcm92aWRlciIsImdyb3VwcyI6WyJBRE1JTiIsIkFVVE9SIiwiU1RBTkRBUkQiXSwiZXhwIjoxNjkzNDY5MzA5LCJpYXQiOjE2OTM0Njg5NDksImVtYWlsIjoibmV1ZXMtM0BlZ2xhZGlsLmRlIn0.PHcSKJPOikt3NXSswWTzNPiVBLM9Wycg45eSV9P4e2L0JfWdmM1l9S9zLETzlYi0ONRH4bK-LrXI6XHRRaVVDHsjhcl8vRoVh3zGG9J-ndpSNjFxrke8l0vNi6TtzAn78PMyReTqWNNi1f0kZjEPeluEQwiBibRYqOXVcMdSFv9soFczq3dEhf1375NSloknq6YQ-AQnzv27EDs1TiJ8UbxioB-onkQ3eKrcY0H4f9GhQ-9HwRLX_mnpDxuSZwJAnUFG_ent0S8ult4SCXpK1k78ap0PGw9Xj9QM7sp_eROHIVQ3ksx-GqJ9oNpy6CvezkDfoWmOztN6JzsYudI6nQ";

	private static final String VALID_JWT_ORDINARY_USER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhNmJmMzhmMi01NDUwLTQ3MjAtOTY4OC05YzIzOWEyZTg3YzgiLCJmdWxsX25hbWUiOiJJY2hlIERvY2hlIiwiaXNzIjoiaGVpa2UyNzE4L2F1dGhwcm92aWRlciIsImdyb3VwcyI6WyJTVEFOREFSRCJdLCJleHAiOjMyNDg2NzM0NTcsImlhdCI6MTY5MzQ2OTg1NywiZW1haWwiOiJpY2hlZG9jaGVAZWdsYWRpbC5kZSJ9.ZS_mm2T9UNs4g97fFFmbMtDQFbm58M24TdbmqnFdWIPS4rKoVRwLF3VKLip--WX0Lre148LmSLhUmTljDr04XTIa3TQVwkryRTRWRAIec4JWtF5NQo07zUmkufy0wYZ-tnhL0MTev_oeETUyIQYhjJvPC9oEMHwVmgB0nk5ZNUCSAArbSgtCWX6lFbjzqK5MWv7iG2xICDSEGWmMOewI0HOEDW0ABosJ9FmY70wPx0YdDpQlFMlQYxXQ8y4K4cx73CKdWDy4Miz5S_kab_lTIPeVNSISn9NvtBrTuVSAUWHHA6vTmTn44DVEeom7vHpggcywGpsCFF4WBxEOzWRQKw";

	private static final String INVALID_JWT_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiODY1ZmM3NS0xYmNmLTQwYzcTZjMy0zMzc0NDgyNmU0OWYiLCJmdWxsX25hbWUiOiJIZWlrZSBXaW5rZWx2b8OfIiwiaXNzIjoiaGVpa2UyNzE4L2F1dGhwcm92aWRlciIsImdyb3VwcyI6WyJBRE1JTiIsIkFVVE9SIiwiU1RBTkRBUkQiXSwiZXhwIjozMjQ4NjcyODI0LCJpYXQiOjE2OTM0NjkyMjQsImVtYWlsIjoibmV1ZXMtM0BlZ2xhZGlsLmRlIn0.Ml03UBtgBgWOkuEnJ5smqTAya0Is3frr1zHpyUbyFBUbN-3iFqCEjmSCd76hJ9oNzju0VildvsgaCpG9ZABWhEmWLM-lM5cEXXCAqPGfXjf-TEhusS-oiEj45xDyN8RC0V-Fg73NTYaivcOOZgartTToKAabu2eXUjYwBhj649ilVmV9cXHtiStoJcxIm1YfxT9WlKUgnvEybw6g_RF8K9vzIUEFiBllCY07SuD6isQVly_7bNm1vIpmOhg9gO6gJFd9TjJIogaZs7ogZqqu3Q3OzBwsksMx8WEMdH_ERVrK6R3IOIqbdb2Yo8SnUBqHzGNEaPM6I_Vn5EWTK6nIMg";

	@Inject
	SessionService sessionService;

	@Nested
	class InitSessionTests {
		@Test
		void should_initSession_work_when_Admin() {

			// Arrange
			// Act 1
			Session session = sessionService.initSession(VALID_JWT_ADMIN);

			// Assert 1
			AuthenticatedUser user = session.getUser();

			assertEquals("Heike Winkelvoß", user.getFullName());
			assertEquals("b865fc75-1bcf-40c7-96c3-33744826e49f", user.getUuid());
			assertEquals(Benutzerart.ADMIN, user.getBenutzerart());

			List<String> rollen = Arrays.asList(user.getRoles());

			assertEquals(3, rollen.size());

			// Act 2
			Session storedSession = sessionService.getAndRefreshSessionIfValid(session.getSessionId());

			// Assert 2
			assertEquals(session, storedSession);

			// Act 3
			sessionService.invalidateSession(session.getSessionId());
			storedSession = sessionService.getAndRefreshSessionIfValid(session.getSessionId());

			// Assert 3
			assertNull(storedSession);

		}

		@Test
		void should_initSession_work_when_OrdinaryUser() {

			// Arrange
			// Act 1
			Session session = sessionService.initSession(VALID_JWT_ORDINARY_USER);

			// Assert 1
			AuthenticatedUser user = session.getUser();

			assertEquals("Iche Doche", user.getFullName());
			assertEquals("a6bf38f2-5450-4720-9688-9c239a2e87c8", user.getUuid());
			assertEquals(Benutzerart.STANDARD, user.getBenutzerart());

			List<String> rollen = Arrays.asList(user.getRoles());

			assertEquals(1, rollen.size());

			// Act 2
			Session storedSession = sessionService.getAndRefreshSessionIfValid(session.getSessionId());

			// Assert 2
			assertEquals(session, storedSession);

			// Act 3
			sessionService.invalidateSession(session.getSessionId());
			storedSession = sessionService.getAndRefreshSessionIfValid(session.getSessionId());

			// Assert 3
			assertNull(storedSession);

		}

		@Test
		void should_initSessionThrowAuthException_when_TokenHasExpired() {

			try {

				sessionService.initSession(EXPIRED_JWT_ADMIN);
				fail("keine AuthException");
			} catch (AuthException e) {

				assertEquals("JWT expired", e.getMessage());
			}

		}

		@Test
		void should_initSessionThrowAuthException_when_TokenIsInvalid() {

			try {

				sessionService.initSession(INVALID_JWT_ADMIN);
				fail("keine AuthException");
			} catch (AuthException e) {

				assertEquals("JWT invalid", e.getMessage());
			}

		}
	}

	@Nested
	class GetBenuterartTests {

		@Test
		void should_getBenutzerartReturn_anonym_when_rolesNull() {

			// Arrange
			String[] roles = null;

			// Act
			Benutzerart result = sessionService.getBenutzerart(roles);

			// Assert
			assertEquals(Benutzerart.ANONYM, result);

		}

		@Test
		void should_getBenutzerartReturn_anonym_when_rolesEmpty() {

			// Arrange
			String[] roles = new String[0];

			// Act
			Benutzerart result = sessionService.getBenutzerart(roles);

			// Assert
			assertEquals(Benutzerart.ANONYM, result);

		}

		@Test
		void should_getBenutzerartReturn_anonym_when_unerwarteteRollen() {

			// Arrange
			String[] roles = new String[] { "PRIVAT", "HAMPELFRAU" };

			// Act
			Benutzerart result = sessionService.getBenutzerart(roles);

			// Assert
			assertEquals(Benutzerart.ANONYM, result);

		}

		@Test
		void should_getBenutzerartReturn_admin_when_rolesContainsADMIN() {

			// Arrange
			String[] roles = new String[] { "STANDARD", "AUTOR", "ADMIN" };

			// Act
			Benutzerart result = sessionService.getBenutzerart(roles);

			// Assert
			assertEquals(Benutzerart.ADMIN, result);

		}

		@Test
		void should_getBenutzerartReturn_autor_when_rolesContainsAUTORButNotADMIN() {

			// Arrange
			String[] roles = new String[] { "STANDARD", "AUTOR" };

			// Act
			Benutzerart result = sessionService.getBenutzerart(roles);

			// Assert
			assertEquals(Benutzerart.AUTOR, result);

		}

		@Test
		void should_getBenutzerartReturn_standard_when_rolesNeigtherContainsAUTORNorADMIN() {

			// Arrange
			String[] roles = new String[] { "STANDARD", "LEHRER" };

			// Act
			Benutzerart result = sessionService.getBenutzerart(roles);

			// Assert
			assertEquals(Benutzerart.STANDARD, result);

		}

		@Test
		void should_getBenutzerartReturn_standard_when_rolesLEHRER() {

			// Arrange
			String[] roles = new String[] { "LEHRER" };

			// Act
			Benutzerart result = sessionService.getBenutzerart(roles);

			// Assert
			assertEquals(Benutzerart.STANDARD, result);

		}

	}

}
