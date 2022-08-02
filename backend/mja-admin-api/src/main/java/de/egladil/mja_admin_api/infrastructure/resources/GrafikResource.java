// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import de.egladil.mja_admin_api.domain.grafiken.GrafikService;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * GrafikResource
 */
@Path("/grafiken/v1")
public class GrafikResource {

	@Inject
	GrafikService grafikService;

	@GET
	@RolesAllowed("ADMIN")
	public MessagePayload pruefeGrafik(@QueryParam(value = "pfad") final String relativerPfad) {

		return grafikService.findGrafik(relativerPfad);
	}

}
