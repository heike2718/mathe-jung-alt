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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.upload.FileUplodService;
import de.egladil.mja_api.domain.upload.FormData;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.web.mja_auth.dto.MessagePayload;
import io.vertx.core.eventbus.EventBus;

/**
 * UploadResource
 */
@Path("/uploads")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Upload")
public class UploadResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadResource.class);

	@Inject
	EventBus bus;

	@Inject
	DevDelayService delayService;

	@Inject
	FileUplodService fileUploadService;

	@SuppressWarnings("removal")
	@POST
	@Path("v1")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "uploadFile",
		summary = "Nimmt eine hochgeladene Datei des UploadTypes (eps) entgegen und speichert sie in dem gewünschten Unterverzeichnis des latex.base.dirs")
	@Parameters({
		@Parameter(name = "type", description = "Typ des Files, der festlegt, wie die Datei verarbeitet bzw. genutzt wird"),
		@Parameter(
			name = "pfad",
			description = "Pfad des Zielverzeichnisses relativ zum konfigurierten latex.base.dir. Der Wert des Parameters muss mit einem / beginnen") })
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

	public Response uploadFile(@Pattern(
		regexp = "^(/[\\da-zA-Z_\\-/]*\\.[\\da-zA-Z_\\-/]*)$", message = "pfad enthält ungültige Zeichen") @QueryParam(
			value = "pfad") final String relativerPfad, @MultipartForm final FormData body) {

		this.delayService.pause();

		FileUpload file = body.file;

		File theFile = file.filePath().toFile();
		UploadData uploadData = new UploadData(relativerPfad, theFile);

		MessagePayload messagePayload = this.fileUploadService.saveTheUpload(uploadData, relativerPfad);

		if (messagePayload.isOk()) {

			return Response.ok(messagePayload).build();
		}

		return Response.serverError().entity(messagePayload).build();

	}

	// @POST
	// @Path("swagger")
	// @Consumes({ MediaType.MULTIPART_FORM_DATA })
	// @RolesAllowed({ "ADMIN", "AUTOR" })
	Response grafikHochladenMitSwagger(@MultipartForm final MultipartBody body) {

		// https://dev.to/felipewind/uploading-a-file-through-swagger-in-quarkus-3l8l

		FileUpload file = body.uploadedFile;

		if (file == null) {

			throw new NullPointerException("da hat was mit der Deserialisierung des bodys nicht geklappt");
		}

		LOGGER.info("upload() " + body.description);

		File theFile = file.filePath().toFile();
		bus.send("file-service", theFile);

		LOGGER.info("upload() before response Accepted");

		return Response
			.accepted()
			.build();
	}

	// Class that will define the OpenAPI schema for the binary type input (upload)
	@Schema(type = SchemaType.STRING, format = "binary")
	public interface UploadItemSchema {
	}

	// Class that will be used to define the request body, and with that
	// it will allow uploading of "N" files
	public class UploadFormSchema {
		public UploadItemSchema uploadedFile;
	}

	// We instruct OpenAPI to use the schema provided by the 'UploadFormSchema'
	// class implementation and thus define a valid OpenAPI schema for the Swagger
	// UI
	@Schema(implementation = UploadFormSchema.class)
	public static class MultipartBody {

		@RestForm
		@PartType(MediaType.TEXT_PLAIN)
		public String description;

		@RestForm
		@PartType(MediaType.MULTIPART_FORM_DATA)
		public FileUpload uploadedFile;
	}

}
