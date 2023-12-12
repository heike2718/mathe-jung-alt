// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.quellen.QuelleMinimalDto;
import de.egladil.mja_api.domain.quellen.QuellenListItem;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * QuellenResource
 */
@Path("mja-api/quellen")
@Tag(name = "Quellen")
public class QuellenResource {

	@Inject
	DevDelayService delayService;

	@Inject
	QuellenService quellenService;

	@GET
	@Path("v2")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "findQuellen", summary = "Gibt alle Quellen zurück, die auf die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			name = "suchstring",
			description = "Freitext zum suchen. Es wird mit like nach diesem Text gesucht") })
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = QuellenListItem.class)))
	// @formatter:off
	public List<QuellenListItem> findQuellenV2(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w ÄÖÜäöüß \\+ \\- \\. \\,]{1,30}$",
		message = "ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") @Valid final String suchstring) {
		// @formatter:on

		// TODO: pagination

		this.delayService.pause();

		return quellenService.findQuellen(suchstring);
	}

	@GET
	@Path("admin/v2")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "getQuelleEingeloggterAdmin",
		summary = "Gibt die Quelle zurück, die zu der eingeloggten Person gehört")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = QuellenListItem.class)))
	@APIResponse(
		name = "Unauthorized",
		description = "nicht authentifiziert",
		responseCode = "401")
	@APIResponse(
		name = "Forbidden",
		description = "nicht autorisiert",
		responseCode = "403")
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "unerwartete Exception",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public QuelleMinimalDto getQuelleEingeloggterAdmin() {

		this.delayService.pause();

		Optional<QuelleMinimalDto> result = this.quellenService.findQuelleForUser();

		if (result.isEmpty()) {

			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
				.entity(MessagePayload.warn("Es gibt noch keine Quelle für Dich als Autor:in. Bitte lass eine anlegen.")).build());
		}

		return result.get();
	}

	@GET
	@Path("{quelleId}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
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
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "quelleId enthält ungültige Zeichen") @PathParam(
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
