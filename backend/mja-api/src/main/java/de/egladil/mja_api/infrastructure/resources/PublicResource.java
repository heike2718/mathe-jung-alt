// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.dto.AnzahlabfrageResponseDto;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruAufgabenDto;
import de.egladil.mja_api.domain.minikaenguru.MinikaenguruService;
import de.egladil.mja_api.domain.quiz.QuizService;
import de.egladil.mja_api.domain.quiz.dto.Quiz;
import de.egladil.mja_api.domain.raetsel.RaetselService;
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
import jakarta.ws.rs.core.Response.Status;

/**
 * PublicResource stellt Resourcen ohne Berechtigungseinschränkungen zur Verfügung.
 */
@Path("mja-api/public")
public class PublicResource {

	@Inject
	MinikaenguruService minikaengiruService;

	@Inject
	QuizService quizService;

	@Inject
	RaetselService raetselService;

	@Path("/minikaenguru/{jahr}/{klasse}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	@Operation(
		operationId = "publicGetAufgabenMinikaenguruwettbewerb",
		summary = "Gibt die Aufgaben eines bestimmten Minikänguru-Wettbewerbs für eine bestimmte Klassenstufe zur Anzeige in anderen Webanwendungen zurück.",
		description = "Nur freigegebene Wettbewerbe werden geliefert.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "jahr",
			description = "Jahr des Wettbewerbs",
			required = true),
		@Parameter(
			in = ParameterIn.PATH,
			name = "klasse",
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

	@GET
	@Path("/quizz/minikaenguru/{jahr}/{klasse}/v1")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@PermitAll
	@Operation(
		operationId = "generateQuizMinikaenguru",
		summary = "Gibt die Aufgaben eines Minikänguru-Wettbewerbs für eine gegebene Klassenstufe zur Verwendung als Quizz zurück.",
		description = "Nur freigegebene Wettbewerbe werden geliefert.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "jahr",
			description = "Jahr des Minikänguru-Wettbewerbs"),
		@Parameter(
			in = ParameterIn.PATH,
			name = "schwierigkeitsgrad",
			description = "Eins von IKID,EINS,ZWEI - die Klassenstufe.",
			required = true) })
	@APIResponse(
		name = "OKResponse",
		description = "Quiz erfolgreich geladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Quiz.class)))
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "ServerError",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	public Response generateQuizMinikaenguru(@Pattern(
		regexp = MjaRegexps.VALID_JAHR,
		message = "jahr enthält ungültige Zeichen oder hat nicht die Länge 4") @PathParam(
			value = "jahr") final String jahr, @PathParam(
				value = "klasse") final Schwierigkeitsgrad schwierigkeitsgrad) {

		Optional<Quiz> optResult = quizService.generateQuiz(Referenztyp.MINIKAENGURU, jahr,
			schwierigkeitsgrad);

		if (optResult.isEmpty()) {

			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(optResult.get()).build();
	}

	@GET
	@Path("/raetsel/anzahl/v1")
	@PermitAll
	@Operation(
		operationId = "getAnzahlFreigegebenerRaetsel",
		summary = "Gibt die Anzahl der zum Abfragezeitpunkt freigegebenen Rätsel zurück.")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AnzahlabfrageResponseDto.class)))
	@APIResponse(
		name = "ServerError",
		responseCode = "500",
		description = "Fehler aufgetreten",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public Response getAnzahlFreigegebenerRaetsel() {
	// @formatter:on

		return Response.ok(raetselService.zaehleFreigegebeneRaetsel()).build();
	}
}
