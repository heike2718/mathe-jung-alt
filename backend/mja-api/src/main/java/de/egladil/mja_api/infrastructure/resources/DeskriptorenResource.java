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
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorSuchkontext;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.deskriptoren.impl.DeskriptorenRepository;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.panache.common.Sort;

/**
 * DeskriptorenResource
 */
@Path("/deskriptoren/v1")
@Tag(name = "Deskriptoren")
public class DeskriptorenResource {

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	@Inject
	DeskriptorenService deskriptorenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(operationId = "loadDeskriptoren", summary = "Liefert die Liste aller Deskriptoren")
	@Parameters({
		@Parameter(name = "kontext", description = "Kontext, zu dem die Deskriptoren geladen werden") })
	@APIResponse(
		name = "LadeDeskriptoren",
		description = "Alle Deskriptoren erfolgreich gelesen.",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = Deskriptor.class)))
	public List<Deskriptor> loadDeskriptoren(@QueryParam(value = "kontext") final DeskriptorSuchkontext suchkontext) {

		List<Deskriptor> trefferliste = deskriptorenRepository.listAll(Sort.ascending("name"));
		return deskriptorenService.filterByKontext(suchkontext, trefferliste);
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
