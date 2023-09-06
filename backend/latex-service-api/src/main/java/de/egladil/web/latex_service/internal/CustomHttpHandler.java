// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_service.internal;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.latex_service.LaTeXCommand;
import de.egladil.web.latex_service.MessagePayload;
import de.egladil.web.latex_service.exception.InvalidInputException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * CustomHttpHandler
 */
public class CustomHttpHandler implements HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(CustomHttpHandler.class);

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final HttpHandlerUtils handlerUtils = new HttpHandlerUtils();

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {

		StringBuilder sb = new StringBuilder();

		try {

			exchange.getResponseHeaders()
				.put(Headers.CONTENT_TYPE, "application/json");

			LaTeXCommand cmd = handlerUtils.getCommand(exchange);

			if (cmd == null) {

				LOGGER.debug("baseUrl called");

				exchange.getResponseSender()
					.send(objectMapper.writeValueAsString(MessagePayload.info("Hallo vom latex-client: usage: " + getUsage())));

			}

			String filename = handlerUtils.getFileName(exchange);
			LOGGER.info("====> calling " + cmd.getShellScript() + " for file " + filename);

			if (filename.endsWith(".tex")) {

				exchange.getResponseSender()
					.send(objectMapper.writeValueAsString(MessagePayload.error("filename bitte ohne .tex!")));

			}

			int exitCode = 0;
			sb.append(sayWhatWillBeDone(cmd, filename));

			exitCode = this.performCommand(cmd, filename);
			LOGGER.info("====> " + cmd.getShellScript() + " " + filename + " exited with exitCode code " + exitCode);

			sb.append(" - exitCode=");
			sb.append(exitCode);

			MessagePayload mp = exitCode == 0 ? MessagePayload.info("fertig: " + sb.toString())
				: MessagePayload.error("Es ist ein Fehler aufgetreten. Guckstu ins log");

			exchange.getResponseSender()
				.send(objectMapper.writeValueAsString(mp));

		} catch (InvalidInputException e) {

			LOGGER.warn(sb.toString());
			LOGGER.error(e.getMessage(), e);

			exchange.getResponseSender()
				.send(objectMapper
					.writeValueAsString(MessagePayload.warn("Request kann nicht verarbeitet werden: " + this.getUsage())));

		} catch (Exception e) {

			LOGGER.warn(sb.toString());
			LOGGER.error(e.getMessage(), e);
			exchange.getResponseSender()
				.send(objectMapper.writeValueAsString(MessagePayload.error("Es ist ein Fehler aufgetreten. Guckstu ins log")));
		}
	}

	private String sayWhatWillBeDone(final LaTeXCommand cmd, final String filename) {

		StringBuilder sb = new StringBuilder();
		sb.append("execute command ");
		sb.append(cmd);
		sb.append(" on file ");
		sb.append(filename);
		return sb.toString();

	}

	private int performCommand(final LaTeXCommand cmd, final String fileName) {

		return new ProcessExecutionService().performCommand(cmd, fileName);
	}

	private String getUsage() {

		StringBuffer sb = new StringBuffer();

		sb.append("der latex-client bietet 2 commands:  ");

		for (LaTeXCommand cmd : LaTeXCommand.values()) {

			sb.append(cmd.getRelativePath());
			sb.append(": ");
			sb.append(cmd.getDescription());
			sb.append("   ");
		}

		sb.append("Das zu transformierende File wird als Query-Parameter 'filename' ohne Suffix .tex erwartet");

		sb.append("    Beispiel: /latex2pdf?filename=02819");

		return sb.toString();
	}
}
