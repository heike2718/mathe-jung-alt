// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.Session;
import de.egladil.web.mja_auth.util.SecUtils;

/**
 * CsrfTokenResource
 */
@Path("/csrftoken/v1")
@Tag(name = "CsrfToken")
public class CsrfTokenResource {

	@Context
	SecurityContext securityContext;

	@GET
	@RolesAllowed("ADMIN")
	@Operation(operationId = "getCsrfToken", summary = "Packt das CSRF-Token in den Header")
	@APIResponse(
		name = "GetCsrfTokenOKResponse",
		description = "CSRF-Token erfolgreich gelesen",
		responseCode = "200")
	public Response getCsrfToken() {

		AuthenticatedUser user = (AuthenticatedUser) securityContext.getUserPrincipal();

		String csrfToken = user == null ? "anonymous-" + SecUtils.generateRandomString() : user.getCsrfToken();

		return Response.ok().header(Session.CSRF_HEADER_NAME, csrfToken).build();
	}

}
