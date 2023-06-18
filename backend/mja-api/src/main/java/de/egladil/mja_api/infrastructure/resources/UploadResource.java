// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.io.File;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
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
import de.egladil.mja_api.domain.upload.FileUplodService;
import de.egladil.mja_api.domain.utils.DevDelayService;

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
		summary = "Nimmt eine hochgeladene Datei des UploadTypes (eps) entgegen und speichert sie in dem gewünschten Unterverzeichnis des latex.base.dirs")
	@Parameters({
		@Parameter(
			name = "pfad",
			description = "Pfad des Zielverzeichnisses relativ zum konfigurierten latex.base.dir. Der Wert des Parameters muss mit einem / beginnen"),
		@Parameter(name = "image", description = "Die Image-Datei, die hochgeladen wird"),
	})
	@APIResponse(
		name = "UploadFileOKResponse",
		description = "Datei erfolgreich hochgeladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "UploadFileConstraintViolation",
		description = "pfad verstößt gegen Validierungsregel oder File ist zu groß oder hat Virus",
		responseCode = "400")
	@APIResponse(
		name = "UploadFileServerException",
		description = "sonstige Fehler",
		responseCode = "500",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response uploadFile(@RestForm("pfad") @Pattern(
		regexp = "^(/[\\da-zA-Z_\\-/]*\\.[\\da-zA-Z_\\-/]*)$",
		message = "pfad enthält ungültige Zeichen") final String relativerPfad, @RestForm("uploadedFile") final FileUpload file) {

		this.delayService.pause();

		File theFile = file.filePath().toFile();
		UploadData uploadData = new UploadData(relativerPfad, theFile);

		MessagePayload messagePayload = this.fileUploadService.saveTheUpload(uploadData, relativerPfad);

		if (messagePayload.isOk()) {

			return Response.ok(messagePayload).build();
		}

		return Response.serverError().entity(messagePayload).build();

		// return Response.serverError().entity(MessagePayload.error("absichtlicher Error")).build();
	}
}
