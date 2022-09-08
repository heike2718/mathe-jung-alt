// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.io.File;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.UploadData;
import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.dto.UploadType;
import de.egladil.mja_api.domain.grafiken.GrafikService;
import de.egladil.mja_api.domain.upload.FormData;
import de.egladil.mja_api.domain.upload.UploadFileService;
import de.egladil.mja_api.domain.upload.UploadScannerDelegate;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.web.mja_auth.dto.MessagePayload;
import io.vertx.core.eventbus.EventBus;

/**
 * UploadResource
 */
@Path("/file-upload/v1")
@Produces(MediaType.APPLICATION_JSON)
public class UploadResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadResource.class);

	@Inject
	EventBus bus;

	@Inject
	DevDelayService delayService;

	@Inject
	UploadFileService uploadFileService;

	@Inject
	GrafikService grafikService;

	@Inject
	UploadScannerDelegate uploadScanner;

	@Context
	SecurityContext securityContext;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("ADMIN")
	public Response grafikHochladen(@QueryParam(value = "type") final UploadType fileType, @QueryParam(
		value = "pfad") final String relativerPfad, @MultipartForm final FormData body) {

		this.delayService.pause();

		FileUpload file = body.file;

		File theFile = file.filePath().toFile();
		UploadData uploadData = new UploadData(relativerPfad, theFile);

		uploadFileService.processFile(uploadData);

		UploadRequestDto uploadRequest = new UploadRequestDto().withBenutzerUuid(securityContext.getUserPrincipal().getName())
			.withUploadData(uploadData).withUploadType(fileType);

		uploadScanner.scanUpload(uploadRequest);

		if (UploadType.GRAFIK == fileType) {

			MessagePayload payload = grafikService.grafikSpeichern(relativerPfad, uploadRequest);
			return Response.ok(payload).build();
		}

		LOGGER.error("unbekannter UploadType {}", fileType);
		return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Path("swagger")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@RolesAllowed("ADMIN")
	public Response grafikHochladenMitSwagger(@MultipartForm final MultipartBody body) {

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
