// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.egladil.mja_admin_api.domain.dto.MessagePayload;
import de.egladil.mja_admin_api.domain.dto.Suchfilter;
import de.egladil.mja_admin_api.domain.quellen.QuelleReadonly;
import de.egladil.mja_admin_api.domain.quellen.QuellenService;

/**
 * QuellenResource
 */
@Path("/quellen/v1")
public class QuellenResource {

	@Inject
	QuellenService quellenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	// @formatter:off
	public List<QuelleReadonly> sucheQuellen(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$",
		message = "ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") final String suchstring,
		@QueryParam(value = "deskriptoren") @Pattern(
				regexp = "^[\\d\\,]{0,200}$",
				message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen und Komma") final String deskriptoren) {
		// @formatter:on

		return quellenService.sucheQuellen(new Suchfilter(suchstring, deskriptoren));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("admin")
	@RolesAllowed({ "ADMIN", "DEFAULT" })
	public QuelleReadonly findQuelleByPerson(@QueryParam(value = "person") final String person) {

		Optional<QuelleReadonly> result = this.quellenService.sucheAdministrator(person);

		if (result.isEmpty()) {

			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Es gibt noch keine Quelle für Sie als Autor:in. Bitte legen Sie eine an.")).build());
		}

		return result.get();
	}

	@GET
	@Path("{quelleId}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public QuelleReadonly findQuelleById(@PathParam(value = "quelleId") final String quelleId) {

		Optional<QuelleReadonly> result = this.quellenService.sucheQuelleMitId(quelleId);

		if (result.isEmpty()) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Es gibt keine Quelle mit dieser UUID")).build());
		}

		return result.get();
	}
}
