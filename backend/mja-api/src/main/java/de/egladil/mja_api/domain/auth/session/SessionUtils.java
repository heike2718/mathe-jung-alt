// =====================================================
// Project: mk-commons
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.config.ConfigService;
import de.egladil.mja_api.domain.exceptions.MjaAuthRuntimeException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

/**
 * SessionUtils
 */
public final class SessionUtils {

	public static final String SESSION_COOKIE_NAME = "JSESSIONID_MJA_ADMIN";

	private static final String SESSION_ID_HEADER = "X-SESSIONID";

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtils.class);

	/**
	 *
	 */
	private SessionUtils() {

	}

	public static byte[] getPublicKey() {

		try (InputStream in = SessionUtils.class.getResourceAsStream("/META-INF/authprov_public_key.pem");
			StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, Charset.forName(ConfigService.DEFAULT_ENCODING));

			return sw.toString().getBytes();
		} catch (IOException e) {

			throw new MjaAuthRuntimeException("Konnte jwt-public-key nicht lesen: " + e.getMessage());
		}

	}

	public static String createIdReference() {

		return "" + UUID.randomUUID().getMostSignificantBits();
	}

	/**
	 * Berechnet den expiresAt-Zeitpunkt mit dem gegebenen idle timout.
	 *
	 * @param  sessionIdleTimeoutMinutes
	 *                                   int Anzahl Minuten, nach denen eine Session als idle weggeräumt wird.
	 * @return                           long
	 */
	public static long getExpiresAt(final int sessionIdleTimeoutMinutes) {

		ZoneId zoneId = ZoneId.systemDefault();
		Instant instant = LocalDateTime.now(zoneId).plus(sessionIdleTimeoutMinutes, ChronoUnit.MINUTES).atZone(zoneId).toInstant();
		return Date.from(instant).getTime();

	}

	public static NewCookie createSessionCookie(final String sessionId) {

		// @formatter:off
		return new NewCookie.Builder(SESSION_COOKIE_NAME)
			.value(sessionId)
			.path("/")
			.domain(null)
			.comment(null)
			.maxAge(360000) // maximum age of the cookie in seconds
			.httpOnly(true)
			.secure(true).build();
		// @formatter:on
	}

	public static NewCookie createSessionInvalidatedCookie() {

		long dateInThePast = LocalDateTime.now(ZoneId.systemDefault()).minus(10, ChronoUnit.YEARS).toEpochSecond(ZoneOffset.UTC);

		// @formatter:off
		return new NewCookie.Builder(SESSION_COOKIE_NAME)
			.maxAge(0) // maximum age of the cookie in seconds
			.expiry(new Date(dateInThePast))
			.version(1)
			.httpOnly(true)
			.secure(true).build();
			// @formatter:on
	}

	public static String getSessionId(final ContainerRequestContext requestContext, final String stage) {

		String sessionIdFromHeader = getSesssionIdFromHeader(requestContext);

		if (sessionIdFromHeader != null) {

			LOGGER.info("sessionIdFromHeader={}", sessionIdFromHeader);
			return sessionIdFromHeader;
		}

		LOGGER.debug("sessionIdFromHeader was null, try to get SessionId from Cookie");
		String sessionIdFromCookie = getSessionIdFromCookie(requestContext);
		LOGGER.info("sessionIdFromCookie={}", sessionIdFromCookie);

		return sessionIdFromCookie;

	}

	/**
	 * @param  requestContext
	 * @return
	 */
	private static String getSesssionIdFromHeader(final ContainerRequestContext requestContext) {

		String sessionIdHeader = requestContext.getHeaderString(SESSION_ID_HEADER);

		if (sessionIdHeader == null) {

			LOGGER.debug("{} dev: Request ohne SessonID-Header", requestContext.getUriInfo());

			return null;
		}

		LOGGER.debug("sessionId={}", sessionIdHeader);
		return sessionIdHeader;
	}

	/**
	 * @param  requestContext
	 * @param  clientPrefix
	 * @return
	 */
	private static String getSessionIdFromCookie(final ContainerRequestContext requestContext) {

		Map<String, Cookie> cookies = requestContext.getCookies();

		Cookie sessionCookie = cookies.get(SESSION_COOKIE_NAME);

		if (sessionCookie != null) {

			return sessionCookie.getValue();
		}

		String path = requestContext.getUriInfo().getAbsolutePath().getPath();

		if (!path.toLowerCase().contains("aufgabensammlungen")) {

			LOGGER.warn("{}: Request ohne {}-Cookie", path, SESSION_COOKIE_NAME);
		}
		return null;
	}
}
