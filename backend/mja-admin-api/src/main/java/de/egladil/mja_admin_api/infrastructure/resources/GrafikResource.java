// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.egladil.mja_admin_api.domain.grafiken.GrafikService;
import de.egladil.mja_admin_api.domain.grafiken.dto.Grafik;

/**
 * GrafikResource
 */
@Path("/grafiken/v1")
@Produces(MediaType.APPLICATION_JSON)
public class GrafikResource {

	@Inject
	GrafikService grafikService;

	@GET
	@RolesAllowed("ADMIN")
	public Grafik pruefeGrafik(@QueryParam(value = "pfad") final String relativerPfad) {

		return grafikService.findGrafik(relativerPfad);
	}

}
