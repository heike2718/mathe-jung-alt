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

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.embeddable_images.EmbeddableImageService;
import de.egladil.mja_api.domain.embeddable_images.dto.CreateEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageResponseDto;
import de.egladil.mja_api.domain.embeddable_images.dto.EmbeddableImageVorschau;
import de.egladil.mja_api.domain.embeddable_images.dto.ReplaceEmbeddableImageRequestDto;
import de.egladil.mja_api.domain.upload.EmbeddableImageUplodService;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * EmbeddableImagesResource
 */
@Path("mja-api/embeddable-images")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "EmbeddableImages")
public class EmbeddableImagesResource {

	@Inject
	DevDelayService delayService;

	@Inject
	EmbeddableImageUplodService uploadService;

	@Inject
	EmbeddableImageService embeddableImageService;

	@GET
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "generatePreview",
		summary = "generiert eine Vorschau (png) für eine Image-Datei, die in ein Rätsel eingebunden wurde oder werden kann.")
	@Parameters({
		@Parameter(
			name = "pfad",
			description = "Pfad des Zielverzeichnisses relativ zum konfigurierten latex.base.dir. Muss mit einem / beginnen",
			required = true) })
	@APIResponse(
		name = "OKResponse",
		description = "Vorschau generiert",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = EmbeddableImageVorschau.class)))
	@APIResponse(
		name = "Bad Request",
		description = "pfad verstößt gegen Validierungsregeln",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Not Found",
		responseCode = "404")
	public Response generatePreview(@NotBlank @Pattern(
		regexp = MjaRegexps.VAILD_RELATIVE_PATH_EPS,
		message = "pfad enthält ungültige Zeichen") @QueryParam(value = "pfad") final String relativerPfad) {

		this.delayService.pause();

		EmbeddableImageVorschau result = embeddableImageService.generatePreview(relativerPfad);
		return Response.ok(result).build();
	}

	@POST
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "createEmbeddableImage",
		summary = "Nimmt eine hochgeladene Datei des UploadTypes (eps) entgegen, speichert sie in einem Unterverzeichnis des latex.base.dirs und generiert den LaTeX-Befehl zum Einbetten der Grafik (includegraphics)")
	@APIResponse(
		name = "OKResponse",
		description = "Datei erfolgreich hochgeladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = EmbeddableImageResponseDto.class)))
	@APIResponse(
		name = "Bad Request",
		description = "Verstoß gegen Validierungsregeln oder File ist zu groß oder hat Virus",
		responseCode = "400")
	@APIResponse(
		name = "Forbidden",
		description = "Akteur ist nicht Besitzer des Rätsels und auch kein ADMIN",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Not Found",
		description = "Das Rätsel existiert nicht",
		responseCode = "404")
	@APIResponse(
		name = "ServerException",
		description = "sonstige Fehler",
		responseCode = "500",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response createEmbeddableImage(@Valid final CreateEmbeddableImageRequestDto requestDto) {

		this.delayService.pause();

		EmbeddableImageResponseDto result = this.uploadService.createEmbeddableImage(requestDto);

		return Response.ok(result).build();
	}

	@PUT
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "updateEmbeddableImage",
		summary = "Ersetzt eine vorhandene Image-Datei, die in ein vorhandenes Rätsel eingebettet ist.")
	@APIResponse(
		name = "OKResponse",
		description = "Datei erfolgreich hochgeladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = EmbeddableImageResponseDto.class)))
	@APIResponse(
		name = "Bad Request",
		description = "raetselId oder pfad verstoßen gegen Validierungsregeln oder File ist zu groß oder hat Virus",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Forbidden",
		description = "Akteur ist nicht Besitzer des Rätsels und auch kein ADMIN",
		responseCode = "403")
	@APIResponse(
		name = "Not Found",
		description = "Das Rätsel oder die Datei exisistieren nicht",
		responseCode = "404")
	@APIResponse(
		name = "ServerException",
		description = "sonstige Fehler",
		responseCode = "500",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response updateEmbeddableImage(@Valid final ReplaceEmbeddableImageRequestDto requestDto) {

		this.delayService.pause();

		EmbeddableImageResponseDto result = this.uploadService.replaceTheEmbeddableImage(requestDto);

		return Response.ok(result).build();

		// return Response.serverError().entity(MessagePayload.error("absichtlicher Error")).build();
	}

}
