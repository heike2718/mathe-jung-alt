// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.egladil.web.mja_auth.dto.AuthResult;
import de.egladil.web.mja_auth.login.LoginLogoutService;
import de.egladil.web.mja_auth.login.LoginUrlService;
import de.egladil.web.mja_auth.session.SessionUtils;

/**
 * SessionResource
 */
@RequestScoped
@Path("session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {

	@Inject
	LoginUrlService loginUrlService;

	@Inject
	LoginLogoutService loginLogoutService;

	@GET
	@Path("authurls/login")
	@PermitAll
	public Response getLoginUrl() {

		return this.loginUrlService.getLoginUrl();

	}

	@POST
	@Path("login")
	@PermitAll
	public Response login(final AuthResult authResult) {

		return loginLogoutService.login(authResult, true);
	}

	@DELETE
	@Path("logout")
	@PermitAll
	public Response logout(@CookieParam(value = SessionUtils.SESSION_COOKIE_NAME) final String sessionId) {

		return loginLogoutService.logout(sessionId);
	}

	@DELETE
	@Path("dev/logout/{sessionId}")
	@PermitAll
	public Response logoutDev(@PathParam(value = "sessionId") final String sessionId) {

		return loginLogoutService.logoutDev(sessionId);
	}

}
