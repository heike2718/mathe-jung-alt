// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.session;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Session
 */
public class Session {

	@JsonProperty
	private String sessionId;

	@JsonProperty
	private long expiresAt;

	@JsonProperty
	private AuthenticatedUser user;

	public static Session createAnonymous(final String sessionId) {

		Session session = new Session();
		session.sessionId = sessionId;
		return session;

	}

	@Override
	public String toString() {

		return "Session [sessionId=" + sessionId + ", expiresAt=" + expiresAt + ", user=" + user + "]";
	}

	@JsonIgnore
	public boolean isAnonym() {

		return user == null;
	}

	/**
	 * In Prod, wo Cookies funktionieren, muss die sessionId im Response-Payload entfernt werden können, da sie über ein Cookie
	 * übertragen wird.
	 */
	public void clearSessionIdInProd() {

		this.sessionId = null;
	}

	@Override
	public int hashCode() {

		return Objects.hash(sessionId);
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (obj == null) {

			return false;
		}

		if (getClass() != obj.getClass()) {

			return false;
		}
		Session other = (Session) obj;
		return Objects.equals(sessionId, other.sessionId);
	}

	/**
	 * @return the expiresAt
	 */
	public long getExpiresAt() {

		return expiresAt;
	}

	/**
	 * @param expiresAt
	 *                  the expiresAt to set
	 */
	public void setExpiresAt(final long expiresAt) {

		this.expiresAt = expiresAt;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {

		return sessionId;
	}

	/**
	 * @return the user
	 */
	public AuthenticatedUser getUser() {

		return user;
	}

	public Session withUser(final AuthenticatedUser user) {

		if (user == null) {

			throw new IllegalArgumentException("user null");
		}

		this.user = user;
		return this;
	}
}
