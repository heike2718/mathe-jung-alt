// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.quiz.QuizService;
import de.egladil.mja_api.domain.quiz.dto.Quiz;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * QuizResource
 */
@Path("/quiz/v1")
@Tag(name = "Quiz")
public class QuizResource {

	@Inject
	QuizService aufgabengruppenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Path("{referenztyp}/{referenz}/{schwierigkeitsgrad}")
	@PermitAll
	@Operation(operationId = "loadQuiz", summary = "Läd ein Quiz")
	@APIResponse(
		name = "LoadQuizOKResponse",
		description = "Quiz erfolgreich geladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Quiz.class)))
	@APIResponse(
		name = "QuizNotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "QuizServerError",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	public Response loadQuiz(@PathParam(
		value = "referenztyp") final Referenztyp referenztyp, @Pattern(regexp = "^[\\w äöüß]{1,20}$") @PathParam(
			value = "referenz") final String referenz, @PathParam(
				value = "schwierigkeitsgrad") final Schwierigkeitsgrad schwierigkeitsgrad) {

		Optional<Quiz> optResult = aufgabengruppenService.findAufgabengruppeByUniqueKey(referenztyp, referenz,
			schwierigkeitsgrad);

		if (optResult.isEmpty()) {

			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(optResult.get()).build();
	}
}
