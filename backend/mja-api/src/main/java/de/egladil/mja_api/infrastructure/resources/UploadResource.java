// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.io.File;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.raetsel.impl.FindPathsGrafikParser;
import de.egladil.mja_api.domain.upload.FileUplodService;
import de.egladil.mja_api.domain.utils.DevDelayService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * UploadResource
 */
@Path("mja-api/uploads")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Upload")
public class UploadResource {

	@Inject
	DevDelayService delayService;

	@Inject
	FileUplodService fileUploadService;

	@POST
	@Path("v1")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "uploadFile",
		summary = "Nimmt eine hochgeladene Datei des UploadTypes (eps) entgegen und speichert sie in dem gewünschten Unterverzeichnis des latex.base.dirs. Form-Parameters=pfad (Pfad des Zielverzeichnisses relativ zum konfigurierten latex.base.dir. Der Wert des Parameters muss mit einem / beginnen), image (Image-Datei)")
	@Parameters({
		@Parameter(
			in = ParameterIn.DEFAULT,
			name = "raetselId", description = "UUID des Rätsels, zu dem die Grafik hochgeladen wird.",
			required = true),
		@Parameter(
			in = ParameterIn.DEFAULT,
			name = "pfad", description = "relativer Pfad zum LaTeX-Verzeichnis, aus dem die eps beim compilieren gelesen wird.",
			required = true),
		@Parameter(
			name = "uploadedFile",
			description = "Das File",
			required = true)
	})
	@APIResponse(
		name = "OKResponse",
		description = "Datei erfolgreich hochgeladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Bad Request",
		description = "raetselId oder pfad verstoßen gegen Validierungsregeln oder File ist zu groß oder hat Virus",
		responseCode = "400")
	@APIResponse(
		name = "Forbidden",
		description = "Akteur ist nicht Besitzer des Rätsels und auch kein ADMIN",
		responseCode = "403")
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
	public Response uploadFile(@RestForm("raetselId") @Pattern(
		regexp = "^[a-f\\d]{4}(?:[a-f\\d]{4}-){4}[a-f\\d]{12}$|neu",
		message = "die raetselId enthält ungültige Zeichen") final String raetselId, @RestForm("pfad") @Pattern(
			regexp = FindPathsGrafikParser.REGEXP_GRAFIK,
			message = "pfad ist nicht akzeptabel") final String relativerPfad, @RestForm("uploadedFile") final FileUpload file) {

		this.delayService.pause();

		File theFile = file.filePath().toFile();
		UploadData uploadData = new UploadData(relativerPfad, theFile);

		MessagePayload messagePayload = this.fileUploadService.saveTheUpload(raetselId, uploadData, relativerPfad);

		if (messagePayload.isOk()) {

			return Response.ok(messagePayload).build();
		}

		return Response.serverError().entity(messagePayload).build();

		// return Response.serverError().entity(MessagePayload.error("absichtlicher Error")).build();
	}
}
