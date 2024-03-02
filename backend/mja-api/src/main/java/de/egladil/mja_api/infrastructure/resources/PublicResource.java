// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruAufgabenDto;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruService;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * PublicResource stellt Resourcen ohne Berechtigungseinschränkungen zur Verfügung.
 */
@Path("public")
public class PublicResource {

	@Inject
	MinikaenguruService minikaengiruService;

	@Path("/minikaenguru/{jahr}/{klasse}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	@Operation(
		operationId = "publicGetAufgabenMinikaenguruwettbewerb",
		description = "gibt die Aufgaben eines bestimmten Minikänguru-Wettbewerbs für eine bestimmte Klassenstufe zurück.Nur freigegebene Wettbewerbe werden geliefert")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "jahr",
			description = "Jahr des Wettbewerbs",
			required = true),
		@Parameter(
			in = ParameterIn.PATH,
			name = "schwierigkeitsgrad",
			description = "Eins von IKID,EINS,ZWEI - die Klassenstufe.",
			required = true) })
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MinikaenguruAufgabenDto.class)))
	@APIResponse(
		name = "BadRequest",
		description = "Input-Validierung ging schief.",
		responseCode = "400")
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "ServerError",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	public Response getAufgabenMinikaenguruwettbewerb(@Pattern(
		regexp = MjaRegexps.VALID_JAHR,
		message = "jahr enthält ungültige Zeichen oder hat nicht die Länge 4") @PathParam(
			value = "jahr") final String jahr, @PathParam(
				value = "klasse") final Schwierigkeitsgrad schwierigkeitsgrad) {

		MinikaenguruAufgabenDto aufgaben = minikaengiruService.getAufgabenFreigegebenerWettbewerb(jahr, schwierigkeitsgrad);

		return Response.ok(aufgaben).build();
	}
}
