// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.quiz.QuizService;
import de.egladil.mja_api.domain.quiz.dto.Quiz;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;

/**
 * QuizzResource
 */
@Path("mja-api/quiz/v1")
@Tag(name = "Quiz", description = "Stellt Aufgaben von Rätselgruppen in verschiedenen Formen als Quiz zur Verfügung.")
public class QuizzResource {

	@Inject
	QuizService quizService;

	@GET
	@Path("{referenztyp}/{referenz}/{schwierigkeitsgrad}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@PermitAll
	@Operation(
		operationId = "generateQuizWithUniqueKey",
		summary = "Generiert ein JSON-Objekt mit allen Aufgaben und Lösungen der Rätselgruppe, die durch die fachlichen Parameter eindeutig bestimmt ist. Das ist eine Methode, um auf die Minikänguru-Wettbewerbe zuzugreifen, ohne deren ID zu kennen. Die API liefert nur Quiz mit dem Status FREIGEGEBEN zurück.")
	@Parameters({
		@Parameter(name = "referenztyp", description = "Kontext zur Interpretation der Referenz"),
		@Parameter(name = "referenz", description = "ID im alten Aufgabenarchiv"),
		@Parameter(
			name = "schwierigkeitsgrad",
			description = "Klassenstufe, für die das Quiz gedacht ist") })
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
	public Response generateQuizWithUniqueKey(@PathParam(
		value = "referenztyp") final Referenztyp referenztyp, @Pattern(
			regexp = "^[\\w äöüß]{1,20}$", message = "referenz enthält ungültige Zeichen") @PathParam(
				value = "referenz") final String referenz, @PathParam(
					value = "schwierigkeitsgrad") final Schwierigkeitsgrad schwierigkeitsgrad) {

		Optional<Quiz> optResult = quizService.generateQuiz(referenztyp, referenz,
			schwierigkeitsgrad);

		if (optResult.isEmpty()) {

			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(optResult.get()).build();
	}

	@GET
	@Path("arbeitsblaetter/PDF/{raetselgruppeID}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "printArbeitsblaetter",
		summary = "Generiert aus der Rätselgruppe mit der gegebenen ID ein PDF. Diese API stellt nur die Rätselgruppen mit Status FREIGEGEBEN bereit. Die Lösungen werden am Ende des PDFs von den Aufgaben separiert gedruckt.")
	@Parameters({
		@Parameter(name = "raetselgruppeID", description = "ID der Rätselgruppe, für das ein Quiz gedruckt wird."),
		@Parameter(
			name = "layoutAntwortvorschlaege",
			description = "Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)")
	})
	@APIResponse(
		name = "PrintQuizFreigegebenOKResponse",
		description = "Quiz erfolgreich geladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = GeneratedFile.class)))
	@APIResponse(
		name = "QuizNotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "QuizServerError",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	@RolesAllowed({ "ADMIN", "AUTOR", "LEHRER", "PRIVAT", "STANDARD" })
	public GeneratedFile printArbeitsblaetter(@PathParam(
		value = "raetselgruppeID") @Pattern(
			regexp = "^[a-fA-F\\d\\-]{1,36}$",
			message = "Pfad (ID) enthält ungültige Zeichen") final String raetselgruppeID, @QueryParam(
				value = "layoutAntwortvorschlaege") @NotNull final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		throw new WebApplicationException(
			Response.status(501).entity(MessagePayload.warn("Die API ist noch nicht implementiert")).build());
	}

}
