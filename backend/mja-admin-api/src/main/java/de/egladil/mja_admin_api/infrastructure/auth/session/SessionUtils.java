// =====================================================
// Project: mk-commons
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.session;

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

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.MjaAdminApiApplication;
import de.egladil.mja_admin_api.domain.error.MjaRuntimeException;

/**
 * SessionUtils
 */
public final class SessionUtils {

	public static final String SESSION_COOKIE_NAME = "MJA_SESSIONID";

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

			IOUtils.copy(in, sw, Charset.forName(MjaAdminApiApplication.DEFAULT_ENCODING));

			return sw.toString().getBytes();
		} catch (IOException e) {

			throw new MjaRuntimeException("Konnte jwt-public-key nicht lesen: " + e.getMessage());
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
		NewCookie sessionCookie = new NewCookie(SESSION_COOKIE_NAME,
			sessionId,
			"/", // path
			null, // domain muss null sein, wird vom Browser anhand des restlichen Responses abgeleitet. Sonst wird das Cookie nicht gesetzt.
			1,  // version
			null, // comment
			7200, // expires (minutes)
			null,
			true, // secure
			true  // httpOnly
			);
		// @formatter:on

		return sessionCookie;
	}

	public static NewCookie createSessionInvalidatedCookie() {

		long dateInThePast = LocalDateTime.now(ZoneId.systemDefault()).minus(10, ChronoUnit.YEARS).toEpochSecond(ZoneOffset.UTC);

		// @formatter:off
		NewCookie invalidationCookie = new NewCookie(SESSION_COOKIE_NAME,
			null,
			null,
			null,
			1,
			null,
			0,
			new Date(dateInThePast),
			true,
			true);
		//@formatter:on

		return invalidationCookie;
	}

	public static String getSessionId(final ContainerRequestContext requestContext, final String stage) {

		return !MjaAdminApiApplication.STAGE_DEV.equals(stage) ? getSessionIdFromCookie(requestContext)
			: getSesssionIdFromHeader(requestContext);

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

		LOGGER.warn("{}: Request ohne {}-Cookie", requestContext.getUriInfo(), SESSION_COOKIE_NAME);
		return null;
	}
}
