// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.auth.login;

import javax.ws.rs.core.Response;

import de.egladil.mja_api.domain.auth.ClientType;

/**
 * LoginUrlService
 */
public interface LoginUrlService {

	/**
	 * @return Response mit der autorisierten Login-Redirect-Url im Payload
	 */
	Response getLoginUrl(ClientType clientType);
}
