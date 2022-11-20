// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.session;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import de.egladil.web.mja_auth.exception.AuthException;
import de.egladil.web.mja_auth.exception.SessionExpiredException;
import de.egladil.web.mja_auth.jwt.JWTService;
import de.egladil.web.mja_auth.jwt.impl.DecodedJWTReader;
import de.egladil.web.mja_auth.util.SecureTokenService;

@ApplicationScoped
public class SessionService {

	public static final int SESSION_IDLE_TIMEOUT_MINUTES = 120;

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

	private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

	@Inject
	JWTService jwtService;

	@Inject
	SecureTokenService secureTokenService;

	/**
	 * Wenn das JWT sagt, ist kein Admin, dann wird eine anonyme Session angelegt.
	 *
	 * @param  jwt
	 * @param  needsToBeAdmin
	 * @return
	 */
	public Session initSession(final String jwt, final boolean needsToBeAdmin) {

		try {

			DecodedJWT decodedJWT = jwtService.verify(jwt, SessionUtils.getPublicKey());

			final DecodedJWTReader jwtReader = new DecodedJWTReader(decodedJWT);

			String[] groups = jwtReader.getGroups();

			String uuid = decodedJWT.getSubject();

			if (needsToBeAdmin) {

				Optional<String> optAdmin = Arrays.stream(groups).filter(g -> "ADMIN".equals(g) || "AUTOR".equals(g)).findFirst();

				if (optAdmin.isEmpty()) {

					LOGGER.warn("User mit Rollen {} ist kein ADMIN => verboten. UUID={}", groups, uuid);
					return this.internalCreateAnonymousSession();
				}
			}

			String fullName = jwtReader.getFullName();

			String userIdReference = uuid.substring(0, 8) + "_" + secureTokenService.createRandomToken();

			AuthenticatedUser authenticatedUser = new AuthenticatedUser(userIdReference, groups, fullName,
				uuid);

			Session session = this.internalCreateAnonymousSession().withUser(authenticatedUser);
			session.setExpiresAt(SessionUtils.getExpiresAt(SESSION_IDLE_TIMEOUT_MINUTES));

			sessions.put(session.getSessionId(), session);

			LOGGER.info("User eingeloggt: {}", session.getUser().toString());

			return session;
		} catch (TokenExpiredException e) {

			LOGGER.error("JWT expired");
			throw new AuthException("JWT expired");
		} catch (JWTVerificationException e) {

			String msg = "Security Thread: JWT " + StringUtils.abbreviate(jwt, 20) + " invalid: " + e.getMessage();
			LOGGER.warn(msg);
			throw new AuthException("JWT invalid");
		}
	}

	/**
	 * @param  sessionId
	 * @return           Session
	 */
	public Session getAndRefreshSessionIfValid(final String sessionId) {

		Session session = sessions.get(sessionId);

		if (session == null) {

			return null;
		}

		LocalDateTime expireDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getExpiresAt()),
			ZoneId.systemDefault()).plusSeconds(5); // bissel Toleranz lassen, oder?
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

		if (now.isAfter(expireDateTime)) {

			sessions.remove(sessionId);
			throw new SessionExpiredException("Die Session ist abgelaufen. Bitte neu einloggen.");
		}

		session.setExpiresAt(SessionUtils.getExpiresAt(SESSION_IDLE_TIMEOUT_MINUTES));

		return session;
	}

	public Session getSessionNullSave(final String sessionId) {

		return sessionId == null ? null : sessions.get(sessionId);
	}

	public void invalidateSession(final String sessionId) {

		if (sessionId == null) {

			LOGGER.debug("invalidateSession ohne sessionId aufgerufen");
			return;
		}

		Session session = this.sessions.remove(sessionId);

		if (session != null && !session.isAnonym()) {

			LOGGER.info("User ausgeloggt: {}", session.getUser().toString());
		}
	}

	private Session internalCreateAnonymousSession() {

		String sessionId = secureTokenService.createRandomToken();
		return Session.createAnonymous(sessionId);
	}
}
