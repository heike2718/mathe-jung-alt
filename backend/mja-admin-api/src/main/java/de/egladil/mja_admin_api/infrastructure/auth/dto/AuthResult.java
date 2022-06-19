// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.auth.dto;

import javax.validation.constraints.NotBlank;

/**
 * AuthResult
 */
public class AuthResult {

	private long expiresAt;

	private String state;

	@NotBlank
	private String nonce;

	@NotBlank
	private String idToken;

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
	 * @return the state
	 */
	public String getState() {

		return state;
	}

	/**
	 * @param state
	 *              the state to set
	 */
	public void setState(final String state) {

		this.state = state;
	}

	/**
	 * @return the nonce
	 */
	public String getNonce() {

		return nonce;
	}

	/**
	 * @param nonce
	 *              the nonce to set
	 */
	public void setNonce(final String nonce) {

		this.nonce = nonce;
	}

	/**
	 * @return the idToken
	 */
	public String getIdToken() {

		return idToken;
	}

	/**
	 * @param idToken
	 *                the idToken to set
	 */
	public void setIdToken(final String idToken) {

		this.idToken = idToken;
	}

}
