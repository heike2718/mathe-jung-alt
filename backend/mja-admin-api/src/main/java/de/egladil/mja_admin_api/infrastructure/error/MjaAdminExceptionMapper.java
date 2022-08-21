// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.error;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.exception.FileUploadException;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.exception.AuthException;
import de.egladil.web.mja_auth.exception.SessionExpiredException;
import de.egladil.web.mja_shared.exceptions.LaTeXCompileException;

/**
 * MjaAdminExceptionMapper
 */
@Provider
public class MjaAdminExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MjaAdminExceptionMapper.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Override
	public Response toResponse(final Throwable exception) {

		if (exception instanceof LaTeXCompileException) {

			LaTeXCompileException ex = (LaTeXCompileException) exception;
			return Response.status(500)
				.entity(MessagePayload.error(MessageFormat.format(applicationMessages.getString("latex.error"), ex.getNameFile())))
				.build();

		}

		if (exception instanceof WebApplicationException) {

			WebApplicationException ex = (WebApplicationException) exception;
			return ex.getResponse();
		}

		if (exception instanceof AuthException) {

			LOGGER.warn(exception.getMessage());
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (exception instanceof SessionExpiredException) {

			return Response.status(440).entity(MessagePayload.warn(exception.getMessage()))
				.build();
		}

		if (exception instanceof FileUploadException) {

			return Response.status(400).entity(MessagePayload.error(exception.getMessage()))
				.build();

		}

		LOGGER.error(exception.getMessage(), exception);

		return Response.status(500).entity(MessagePayload.error(applicationMessages.getString("general.internalServerError")))
			.build();
	}

}
