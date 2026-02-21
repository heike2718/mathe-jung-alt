// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.minikaenguru.MinikaenguruAufgabenKlassenstufeDto;
import de.egladil.raetselbaukasten.domain.minikaenguru.MinikaenguruService;
import de.egladil.raetselbaukasten.domain.validation.MjaRegexps;

/**
 * RestrictedResource
 */
@Path("api/restricted")
public class RestrictedResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestrictedResource.class);

    @Inject
    MinikaenguruService minikaenguruService;

    @Path("/minikaenguru/{jahr}/{klasse}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Operation(
            operationId = "gibt die Aufgaben eines bestimmten Minikänguru-Wettbewerbs für eine bestimmte Klassenstufe zurück. Autorisiert wird mittels S2S Basic authentiation")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "jahr", description = "Jahr des Wettbewerbs", required = true),
            @Parameter(
                    in = ParameterIn.PATH,
                    name = "klasse",
                    description = "Eins von IKID,EINS,ZWEI - die Klassenstufe.",
                    required = true),
            @Parameter(in = ParameterIn.HEADER, name = "X-CLIENT-ID", description = "ClientId", required = true),
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = " Authorization",
                    description = "Basic Authorization-Header. clientId:clientSecret Base64",
                    required = true) })
    @APIResponse(
            name = "OKResponse",
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MinikaenguruAufgabenKlassenstufeDto.class)))
    @APIResponse(name = "Forbidden", description = "S2S-Authentifizierung ging schief", responseCode = "401")
    @APIResponse(
            name = "BadRequest",
            description = "Input-Validierung ging schief oder der Wettbewerb hat nicht den erforderlichen Status.",
            responseCode = "400")
    @APIResponse(name = "NotFound", description = "Gibt es nicht", responseCode = "404")
    @APIResponse(
            name = "ServerError",
            description = "Serverfehler",
            responseCode = "500",
            content = @Content(schema = @Schema(implementation = MessagePayload.class)))
    // @formatter:off
	public Response getAufgabenMinikaenguruwettbewerb(
		@Pattern(regexp = MjaRegexps.VALID_JAHR, message = "jahr enthält ungültige Zeichen oder hat nicht die Länge 4")
			@PathParam(value = "jahr") final String jahr,
		@PathParam(value = "klasse") final Schwierigkeitsgrad schwierigkeitsgrad) {
	// @formatter:on

        LOGGER.info("called with Parameters jahr={}, klasse={}", jahr, schwierigkeitsgrad);

        MinikaenguruAufgabenKlassenstufeDto aufgaben = minikaenguruService
                .getAufgabenNichtFreigegebenerWettbewerb(jahr, schwierigkeitsgrad);

        LOGGER.info("ok with Parameters jahr={}, klasse={}", jahr, schwierigkeitsgrad);

        return Response.ok(aufgaben).build();
    }

}
