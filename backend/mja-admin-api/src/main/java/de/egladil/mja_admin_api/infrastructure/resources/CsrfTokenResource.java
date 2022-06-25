// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.Session;

/**
 * CsrfTokenResource
 */
@Path("/csrftoken/v1")
public class CsrfTokenResource {

	@Context
	SecurityContext securityContext;

	@GET
	@RolesAllowed("ADMIN")
	public Response getCsrfToken() {

		AuthenticatedUser user = (AuthenticatedUser) securityContext.getUserPrincipal();
		return Response.ok().header(Session.CSRF_HEADER_NAME, user.getCsrfToken()).build();
	}

}
