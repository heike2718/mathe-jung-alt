// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

	@Inject
	AuthenticationContext authCtx;

	@GET
	@Path("autor/v2")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "getAuthenticatedUserAsQuelle",
		summary = "Gibt den angemeldeten User als Quelle von Eigenkreationen zurück. Falls es noch keine Quelle gibt, wird eine angelegt.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = QuelleDto.class)))
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
	public Response getAuthenticatedUserAsQuelle() {

		this.delayService.pause();

		QuelleDto result = this.quellenService.findOrCreateQuelleAutor();

		return Response.ok(result).build();
	}
}
