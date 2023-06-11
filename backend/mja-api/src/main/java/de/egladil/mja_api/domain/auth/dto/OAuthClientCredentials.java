// =====================================================
// Project: mja-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.auth.util.SecUtils;

/**
 * OAuthClientCredentials
 */
@Schema(name = "OAuthClientCredentials", description = "credentials des OAuth2-Clients beim authprovider")
public class OAuthClientCredentials {

	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9+=]*")
	@Size(max = 50)
	@Schema(description = "ID des authprovider-Clients")
	private String clientId;

	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9+=]*")
	@Size(max = 50)
	@Schema(description = "secret des authprovider-Clients")
	private String clientSecret;

	@Pattern(regexp = "[a-zA-Z0-9\\-]*")
	@Size(max = 36)
	@Schema(
		description = "zur Authentifizierung des Clients generierter String, der vom authprovider unverändert zurückgegeben wird")
	private String nonce;

	public static OAuthClientCredentials create(final String clientId, final String clientSecret, final String nonce) {

		OAuthClientCredentials result = new OAuthClientCredentials();
		result.clientId = clientId;
		result.clientSecret = clientSecret;
		result.nonce = nonce;
		return result;

	}

	public String getClientId() {

		return clientId;
	}

	public void setClientId(final String clientId) {

		this.clientId = clientId;
	}

	public String getClientSecret() {

		return clientSecret;
	}

	public void setClientSecret(final String clientSecret) {

		this.clientSecret = clientSecret;
	}

	public String getNonce() {

		return nonce;
	}

	public void setNonce(final String nonce) {

		this.nonce = nonce;
	}

	public void clean() {

		clientId = SecUtils.wipe(clientId);
		clientSecret = SecUtils.wipe(clientSecret);
	}

}
