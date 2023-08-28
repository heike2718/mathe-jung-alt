// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.grafiken.Grafik;
import de.egladil.mja_api.domain.grafiken.GrafikService;
import de.egladil.mja_api.domain.utils.DevDelayService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * GrafikResource
 */
@Path("mja-api/grafiken")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Grafikvorschau")
public class GrafikResource {

	@Inject
	DevDelayService delayService;

	@Inject
	GrafikService grafikService;

	@GET
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findGrafik", summary = "liefert eine Grafikvorschau (png) für ein Image, das in LaTeX eingebunden wird.")
	@Parameters({
		@Parameter(
			name = "pfad",
			description = "Pfad des Zielverzeichnisses relativ zum konfigurierten latex.base.dir. Der Wert des Parameters muss mit einem / beginnen") })
	@APIResponse(
		name = "FindGrafikOKResponse",
		description = "Grafikvorschau geladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Grafik.class)))
	public Grafik findGrafik(@Pattern(
		regexp = "^(/[\\da-zA-Z_\\-/]*\\.[\\da-zA-Z_\\-/]*)$",
		message = "pfad enthält ungültige Zeichen") @QueryParam(value = "pfad") final String relativerPfad) {

		this.delayService.pause();

		return grafikService.findGrafik(relativerPfad);
	}
}
