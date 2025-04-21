// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.infrastructure.error;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.exceptions.AuthException;
import de.egladil.raetselbaukasten.domain.exceptions.LaTeXCompileException;
import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.exceptions.SessionExpiredException;
import de.egladil.raetselbaukasten.domain.exceptions.UploadFormatException;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * RaetselbaukastenExceptionMapper.
 */
@Provider
public class RaetselbaukastenExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselbaukastenExceptionMapper.class);

	private final ResourceBundle applicationMessages = ResourceBundle.getBundle("ApplicationMessages", Locale.GERMAN);

	@Inject
	AuthenticationContext authCtx;

	@Override
	public Response toResponse(final Throwable exception) {

		LOGGER.debug(exception.getMessage(), exception);

		if (exception instanceof LaTeXCompileException) {

			LaTeXCompileException ex = (LaTeXCompileException) exception;

			String message = "";

			if (authCtx != null && authCtx.getUser().isAdminOrAutor()) {
				message = MessageFormat.format(applicationMessages.getString("admin.latex.error"), ex.getNameFile());
			} else {
				message = applicationMessages.getString("latex.error");
			}
			return Response.status(500).entity(MessagePayload.error(message)).build();

		}

		if (exception instanceof WebApplicationException) {

			WebApplicationException ex = (WebApplicationException) exception;

			if (ex.getResponse().getStatus() == 403) {

				LOGGER.warn(
					"403: könnte auch eine fehlende mod-security-Konfiguration sein, wenn es ein PUT oder DELETE-Request war");
				return Response.status(403).entity(MessagePayload.error("Diese Aktion ist nicht erlaubt")).build();
			}

			return ex.getResponse();
		}

		if (exception instanceof AuthException) {

			LOGGER.warn(exception.getMessage());
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (exception instanceof SessionExpiredException) {

			return Response.status(440).entity(MessagePayload.warn(exception.getMessage())).build();
		}

		if (exception instanceof UploadFormatException) {

			return Response.status(Status.BAD_REQUEST).entity(MessagePayload.error(exception.getMessage())).build();

		}

		LOGGER.error(exception.getMessage(), exception);

		if (exception instanceof MjaRuntimeException) {

			return Response.status(500).entity(MessagePayload.error(exception.getMessage())).build();
		}

		return Response.status(500).entity(MessagePayload.error(applicationMessages.getString("general.internalServerError")))
			.build();
	}

}
