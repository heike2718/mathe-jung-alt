// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.error;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mathe_jung_alt_ws.domain.dto.Message;
import de.egladil.mathe_jung_alt_ws.domain.error.LaTeXCompileException;
import de.egladil.mathe_jung_alt_ws.domain.error.MjaRuntimeException;

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
				.entity(Message.error(MessageFormat.format(applicationMessages.getString("latex.error"), ex.getNameFile())))
				.build();

		}

		if (exception instanceof MjaRuntimeException) {

			// nicht loggen, wurde schon
		} else {

			LOGGER.error(exception.getMessage(), exception);
		}

		return Response.status(500).entity(Message.error(applicationMessages.getString("general.internalServerError")))
			.build();
	}

}
