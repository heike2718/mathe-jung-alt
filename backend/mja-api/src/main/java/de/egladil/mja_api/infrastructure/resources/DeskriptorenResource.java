// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

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

/**
 * DeskriptorenResource
 */
@Path("mja-api/deskriptoren")
@Tag(name = "Deskriptoren")
public class DeskriptorenResource {

	@Context
	SecurityContext securityContext;

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	@Inject
	DeskriptorenService deskriptorenService;

	@Path("v2")
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@RolesAllowed({ "ADMIN", "AUTOR", "STANDARD" })
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
	public List<DeskriptorUI> loadDeskriptorenV2() {

		boolean admin = securityContext.isUserInRole("ADMIN") || securityContext.isUserInRole("AUTOR");

		return deskriptorenService.loadDeskriptorenRaetsel(admin);
	}

	@GET
	@Path("ids")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed({ "ADMIN", "AUTOR" })
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
	public String transformDeskriptorenStringToOrdinal(@QueryParam(value = "deskriptoren") @Pattern(
		regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
		message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren) {

		return deskriptorenService.transformToDeskriptorenOrdinal(deskriptoren);
	}
}
