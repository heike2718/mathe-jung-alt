// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import java.util.List;
import java.util.Optional;

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

import de.egladil.mathe_jung_alt_ws.domain.dto.Message;
import de.egladil.mathe_jung_alt_ws.domain.dto.Suchfilter;
import de.egladil.mathe_jung_alt_ws.domain.quellen.QuelleReadonly;
import de.egladil.mathe_jung_alt_ws.domain.quellen.QuellenService;

/**
 * AdminQuellenResource
 */
@Path("/admin/quellen/v1")
public class AdminQuellenResource {

	@Inject
	QuellenService quellenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
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
	public QuelleReadonly findQuelleByPerson(@QueryParam(value = "person") final String person) {

		Optional<QuelleReadonly> result = this.quellenService.sucheAdministrator(person);

		if (result.isEmpty()) {

			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
				.entity(Message.error("Diese Quelle gibt es noch nicht: bitte zuerst anlegen")).build());
		}

		return result.get();
	}

	@GET
	@Path("{quelleId}")
	@Produces(MediaType.APPLICATION_JSON)
	public QuelleReadonly findQuelleById(@PathParam(value = "quelleId") final String quelleId) {

		Optional<QuelleReadonly> result = this.quellenService.sucheQuelleMitId(quelleId);

		if (result.isEmpty()) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(Message.error("Es gibt keine Quelle mit dieser UUID")).build());
		}

		return result.get();
	}
}
