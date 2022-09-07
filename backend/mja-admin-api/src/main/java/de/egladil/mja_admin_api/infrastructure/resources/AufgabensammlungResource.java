// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.egladil.mja_admin_api.domain.sammlungen.AufgabensammlungService;
import de.egladil.mja_admin_api.domain.sammlungen.Referenztyp;
import de.egladil.mja_admin_api.domain.sammlungen.Schwierigkeitsgrad;
import de.egladil.mja_admin_api.domain.sammlungen.dto.Aufgabensammlung;

/**
 * AufgabensammlungResource
 */
@Path("/aufgabensammlungen/v1")
public class AufgabensammlungResource {

	@Inject
	AufgabensammlungService aufgabensammlungService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{referenztyp}/{referenz}/{schwierigkeitsgrad}")
	@PermitAll
	public Response getAufgabensammlung(@PathParam(
		value = "referenztyp") final Referenztyp referenztyp, @Pattern(regexp = "^[\\w ]{1,20}$") @PathParam(
			value = "referenz") final String referenz, @PathParam(
				value = "schwierigkeitsgrad") final Schwierigkeitsgrad schwierigkeitsgrad) {

		Optional<Aufgabensammlung> optResult = aufgabensammlungService.findAufgabensammlungByUniqueKey(referenztyp, referenz,
			schwierigkeitsgrad);

		if (optResult.isEmpty()) {

			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(optResult.get()).build();
	}
}
