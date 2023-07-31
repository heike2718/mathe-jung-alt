// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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

/**
 * GrafikResource
 */
@Path("mja-api/grafiken/v1")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Grafikvorschau")
public class GrafikResource {

	@Inject
	DevDelayService delayService;

	@Inject
	GrafikService grafikService;

	@GET
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
