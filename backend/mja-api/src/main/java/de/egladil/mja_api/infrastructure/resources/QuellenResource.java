// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

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

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.quellen.QuelleReadonly;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * QuellenResource
 */
@Path("/quellen/v1")
@Tag(name = "Quellen")
public class QuellenResource {

	@Inject
	DevDelayService delayService;

	@Inject
	QuellenService quellenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findQuellen", summary = "Gibt alle Quellen zurück, die auf die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			name = "suchstring",
			description = "Freitext zum suchen. Es wird mit like nach diesem Text gesucht"),
		@Parameter(name = "deskriptoren", description = "kommaseparierte Liste von Namen von Deskriptoren") })
	@APIResponse(
		name = "FindQuellenOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = QuelleReadonly.class)))
	// @formatter:off
	public List<QuelleReadonly> findQuellen(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$",
		message = "ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") final String suchstring,
		@QueryParam(value = "deskriptoren") @Pattern(
				regexp = "^[\\d\\,]{0,200}$",
				message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen und Komma") final String deskriptoren) {
		// @formatter:on

		// TODO: pagination

		this.delayService.pause();

		return quellenService.sucheQuellen(new Suchfilter(suchstring, deskriptoren));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Path("admin")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findQuelleByPerson",
		summary = "Gibt die Quelle zurück, die 'Vorname Nachname' der eingeloggten Person gehört")
	@Parameters({
		@Parameter(
			name = "person",
			description = "Vorname Nachname einer Person, so wie sie nach dem Einloggen in der mja-admin-app angezeigt wird") })
	@APIResponse(
		name = "FindQuelleByPersonOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = QuelleReadonly.class)))
	@APIResponse(
		name = "QuellePersonNotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public QuelleReadonly findQuelleByPerson(@Pattern(
		regexp = "[\\w äöüßÄÖÜ\\-@&,.()\"]*",
		message = "person enthält ungültige Zeichen") @QueryParam(value = "person") final String person) {

		this.delayService.pause();

		Optional<QuelleReadonly> result = this.quellenService.sucheAdministrator(person);

		if (result.isEmpty()) {

			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Es gibt noch keine Quelle für Sie als Autor:in. Bitte legen Sie eine an.")).build());
		}

		return result.get();
	}

	@GET
	@Path("{quelleId}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findQuelleById",
		summary = "Gibt die Quelle mit der gegebenen ID zurück")
	@Parameters({
		@Parameter(
			name = "quelleId",
			description = "technische ID der Quelle") })
	@APIResponse(
		name = "FindQuelleByIDOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = QuelleReadonly.class)))
	@APIResponse(
		name = "QuelleByIDNotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public QuelleReadonly findQuelleById(@Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$", message = "quelleId enthält ungültige Zeichen") @PathParam(
			value = "quelleId") final String quelleId) {

		this.delayService.pause();

		Optional<QuelleReadonly> result = this.quellenService.sucheQuelleMitId(quelleId);

		if (result.isEmpty()) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Es gibt keine Quelle mit dieser UUID")).build());
		}

		return result.get();
	}
}
