// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.quellen.QuelleMinimalDto;
import de.egladil.mja_api.domain.quellen.QuellenListItem;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * QuellenResource
 */
@Path("quellen")
@Tag(name = "Quellen")
public class QuellenResource {

	@Context
	SecurityContext securityContext;

	@Inject
	DevDelayService delayService;

	@Inject
	QuellenService quellenService;

	@Path("v1")
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findQuellenV1", summary = "Gibt alle Quellen zurück, die auf die gegebene Suchanfrage passen.",
		deprecated = true)
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
			schema = @Schema(type = SchemaType.ARRAY, implementation = QuellenListItem.class)))
	// @formatter:off
	public List<QuellenListItem> findQuellenV1(
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
	@Path("v2")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findQuellen", summary = "Gibt alle Quellen zurück, die auf die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			name = "suchstring",
			description = "Freitext zum suchen. Es wird mit like nach diesem Text gesucht") })
	@APIResponse(
		name = "FindQuellenOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = QuellenListItem.class)))
	// @formatter:off
	public List<QuellenListItem> findQuellenV2(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$",
		message = "ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") @Valid final String suchstring) {
		// @formatter:on

		// TODO: pagination

		this.delayService.pause();

		return quellenService.findQuellen(suchstring);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Path("admin/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findOwnQuelle",
		summary = "Gibt die Quelle zurück, die zu der eingeloggten Person gehört",
		deprecated = true)
	@APIResponse(
		name = "FindOwnQuelleOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = QuellenListItem.class)))
	@APIResponse(
		name = "OwnQuelleNotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@Deprecated
	public QuellenListItem findOwnQuelle() {

		this.delayService.pause();

		String userId = securityContext.getUserPrincipal().getName();
		Optional<QuellenListItem> result = this.quellenService.sucheQuelleMitUserID(userId);

		if (result.isEmpty()) {

			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Es gibt noch keine Quelle für Dich als Autor:in. Bitte lass eine anlegen.")).build());
		}

		return result.get();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Path("admin/v2")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findOwnQuelle",
		summary = "Gibt die Quelle zurück, die zu der eingeloggten Person gehört")
	@APIResponse(
		name = "FindOwnQuelleOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = QuellenListItem.class)))
	@APIResponse(
		name = "OwnQuelleNotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public QuelleMinimalDto getQuelleEingeloggterAdmin() {

		this.delayService.pause();

		String userId = securityContext.getUserPrincipal().getName();
		Optional<QuelleMinimalDto> result = this.quellenService.findQuelleForUser(userId);

		if (result.isEmpty()) {

			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Es gibt noch keine Quelle für Dich als Autor:in. Bitte lass eine anlegen.")).build());
		}

		return result.get();
	}

	@GET
	@Path("v1/{quelleId}")
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
			schema = @Schema(implementation = QuellenListItem.class)))
	@APIResponse(
		name = "QuelleByIDNotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public QuellenListItem findQuelleById(@Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$", message = "quelleId enthält ungültige Zeichen") @PathParam(
			value = "quelleId") final String quelleId) {

		this.delayService.pause();

		Optional<QuellenListItem> result = this.quellenService.sucheQuelleMitId(quelleId);

		if (result.isEmpty()) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Es gibt keine Quelle mit dieser UUID")).build());
		}

		return result.get();
	}
}
