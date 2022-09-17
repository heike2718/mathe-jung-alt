// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.dto;

import javax.validation.constraints.NotBlank;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * AuthResult
 */
@Schema(name = "AuthResult", description = "Ergebnis der Authentifizierung beim authprovider")
public class AuthResult {

	@Schema(description = "Gültigkeit des generierten Einmaltokens")
	private long expiresAt;

	@Schema(description = "ein Kontext, also Login oder SignUp")
	private String state;

	@NotBlank
	@Schema(description = "für die Autorisierung generierter String, der vom authprovider unverändert zurückgegeben wird")
	private String nonce;

	@NotBlank
	@Schema(description = "Einmaltoken, mit dem sich der authprovider-Client das JWT holen kann")
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
