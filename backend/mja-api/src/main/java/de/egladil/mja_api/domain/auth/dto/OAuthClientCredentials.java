// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.auth.util.SecUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {

			return true;
		}

		if (!(obj instanceof OAuthClientCredentials)) {

			return false;
		}
		OAuthClientCredentials other = (OAuthClientCredentials) obj;

		if (clientId == null) {

			if (other.clientId != null) {

				return false;
			}
		} else if (!clientId.equals(other.clientId)) {

			return false;
		}
		return true;
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
