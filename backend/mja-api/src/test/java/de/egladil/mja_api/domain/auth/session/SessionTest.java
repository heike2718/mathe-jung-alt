// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * SessionTest
 */
@QuarkusTest
public class SessionTest {

	@Test
	void should_sessionsBeEqual_when_equalSessionId() {

		// Arrange
		Session session_A = Session.createAnonymous("A");

		Session session_B = Session.createAnonymous("A");

		// Assert
		assertEquals(session_A, session_B);
		assertEquals(session_A.hashCode(), session_B.hashCode());

	}

	@Test
	void should_sessionNotBeEqual_toNull() {

		// Arrange
		Session session_A = Session.createAnonymous("A");

		// Assert
		assertFalse(session_A.equals(null));

	}

	@Test
	void should_withUserThrowException_when_userNUll() {

		try {

			Session.createAnonymous("A").withUser(null);
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {

			assertEquals("user null", e.getMessage());
		}
	}

	@Test
	void should_createSessionWithUserWork() {

		// Arrange
		AuthenticatedUser user = AuthenticatedUser.createAnonymousUser();

		// Act
		Session session = Session.createAnonymous("A").withUser(user);

		/// Assert
		assertNotNull(session);
		assertEquals("Gast", session.getUser().getFullName());

	}

}
