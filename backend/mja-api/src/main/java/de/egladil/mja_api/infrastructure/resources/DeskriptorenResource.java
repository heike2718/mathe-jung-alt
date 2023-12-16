// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorUI;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.deskriptoren.impl.DeskriptorenRepository;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * DeskriptorenResource
 */
@Path("mja-api/deskriptoren")
@Tag(name = "Deskriptoren")
public class DeskriptorenResource {

	@Inject
	AuthenticationContext authCtx;

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	@Inject
	DeskriptorenService deskriptorenService;

	@GET
	@Path("v2")
	@Authenticated
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "loadDeskriptorenV2",
		summary = "Liefert die Liste aller Deskriptoren. Je nach Rolle werden nur die public oder alle geladen.")
	@APIResponse(
		name = "LadeDeskriptoren",
		description = "Alle Deskriptoren erfolgreich gelesen.",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = DeskriptorUI.class)))
	public Response loadDeskriptorenV2() {

		return Response.ok(deskriptorenService.loadDeskriptoren()).build();
	}

	@GET
	@Path("ids")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Produces(MediaType.TEXT_PLAIN)
	@Operation(
		operationId = "transformToDeskriptorenOrdinal",
		summary = "Wandelt Deskriptoren in eine kommaseparierte Liste ihrer IDs um")
	@Parameters({
		@Parameter(name = "deskriptoren", description = "kommaseparierte Liste von Namen von Deskriptoren") })
	@APIResponse(
		name = "TransformDeskriptoren",
		description = "Deskriptoren erfolgreich transformiert.",
		responseCode = "200",
		content = @Content(
			mediaType = "text/plain",
			schema = @Schema(implementation = String.class)))
	public Response transformDeskriptorenStringToOrdinal(@QueryParam(value = "deskriptoren") @Pattern(
		regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
		message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren) {

		return Response.ok(deskriptorenService.transformToDeskriptorenOrdinal(deskriptoren)).build();
	}
}
