// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.error;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.error.LaTeXCompileException;
import de.egladil.mja_admin_api.domain.error.MjaRuntimeException;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.exception.AuthException;

/**
 * MjaExceptionMapper
 */
@Provider
public class MjaExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MjaExceptionMapper.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Override
	public Response toResponse(final Throwable exception) {

		if (exception instanceof LaTeXCompileException) {

			LaTeXCompileException ex = (LaTeXCompileException) exception;
			return Response.status(500)
				.entity(MessagePayload.error(MessageFormat.format(applicationMessages.getString("latex.error"), ex.getNameFile())))
				.build();

		}

		if (exception instanceof AuthException) {

			LOGGER.warn("CSRF: " + exception.getMessage());
			return Response.status(Status.FORBIDDEN).build();
		}

		if (exception instanceof MjaRuntimeException) {

			// nicht loggen, wurde schon
		} else {

			LOGGER.error(exception.getMessage(), exception);
		}

		return Response.status(500).entity(MessagePayload.error(applicationMessages.getString("general.internalServerError")))
			.build();
	}

}
