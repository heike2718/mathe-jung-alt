// =====================================================
// Project: mja-api
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.eventbus.EventBus;

/**
 * SwaggerResource
 */
@Path("mja-api/swagger")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class SwaggerResource {

	public static final Logger LOGGER = LoggerFactory.getLogger(SwaggerResource.class);

	@Inject
	EventBus bus;

	@POST
	@Path("upload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@RolesAllowed({ "ADMIN", "AUTOR" })
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
