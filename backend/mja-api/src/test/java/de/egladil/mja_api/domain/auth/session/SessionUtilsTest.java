// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.session;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * SessionUtilsTest
 */
@QuarkusTest
public class SessionUtilsTest {

	@Test
	void should_getPublicKeyWork() {

		// Act
		byte[] result = SessionUtils.getPublicKey();

		// Arrange
		String asString = new String(result);

		assertTrue(asString.startsWith("-----BEGIN PUBLIC KEY-----"));
		assertTrue(asString.contains("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwT4s/a90Wst40aAReBhm"));
		assertTrue(asString.contains("-----END PUBLIC KEY-----"));

	}

	@Test
	void should_createIdReferenceWork() {

		assertFalse(SessionUtils.createIdReference().isBlank());

	}

	@Test
	void should_expiresAtWork() {

		// Arrange
		ZoneId zoneId = ZoneId.systemDefault();
		Instant instant = LocalDateTime.now(zoneId).plus(119, ChronoUnit.MINUTES).atZone(zoneId).toInstant();
		long expectedMinLimit = Date.from(instant).getTime();

		// Act
		long expiresAt = SessionUtils.getExpiresAt(120);

		// Assert
		assertTrue(expiresAt > expectedMinLimit);

	}

	@Test
	void should_createSessionCookieWork() {

		// Arrange
		String sessionId = SessionUtils.createIdReference();

		// Act + Assert
		assertNotNull(SessionUtils.createSessionCookie(sessionId));
	}

	@Test
	void should_createSessionInvalidatedCookieWork() {

		// Act + Assert
		assertNotNull(SessionUtils.createSessionInvalidatedCookie());
	}

}
