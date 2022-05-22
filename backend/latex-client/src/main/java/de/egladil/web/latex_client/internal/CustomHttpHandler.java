// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_client.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.web.latex_client.LaTeXCommand;
import de.egladil.web.latex_client.MessagePayload;
import de.egladil.web.latex_client.exception.InvalidInputException;
import de.egladil.web.latex_client.exception.LaTeXClientException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * CustomHttpHandler
 */
public class CustomHttpHandler implements HttpHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpHandler.class);

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final HttpHandlerUtils handlerUtils = new HttpHandlerUtils();

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {

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

			if (filename.endsWith(".tex")) {

				exchange.getResponseSender()
					.send(objectMapper.writeValueAsString(MessagePayload.error("filename bitte ohne .tex!")));

			}

			StringBuilder sb = new StringBuilder();
			int exitCode = 0;
			sb.append(sayWhatWillBeDone(cmd, filename));

			exitCode = this.performCommand(cmd, filename);
			sb.append(" - exitCode=");
			sb.append(exitCode);

			MessagePayload mp = exitCode == 0 ? MessagePayload.info("fertig: " + sb.toString())
				: MessagePayload.error("Es ist ein Fehler aufgetreten. Guckstu ins log");

			exchange.getResponseSender()
				.send(objectMapper.writeValueAsString(mp));

		} catch (InvalidInputException e) {

			exchange.getResponseSender()
				.send(objectMapper
					.writeValueAsString(MessagePayload.warn("Request kann nicht verarbeitet werden: " + this.getUsage())));

		} catch (LaTeXClientException e) {

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


	// private int performCommand(final LaTeXCommand cmd, final String fileName) {

	// 	List<String> cmdList = new ArrayList<String>();
	// 	cmdList.add("sh");
	// 	cmdList.add(cmd.getShellScript());
	// 	cmdList.add(fileName);

	// 	ProcessBuilder processBuilder = new ProcessBuilder();
	// 	processBuilder.redirectErrorStream(true);
	// 	processBuilder.command(cmdList);
	// 	processBuilder.inheritIO();

	// 	Process process = null;

	// 	try {

	// 		process = processBuilder.start();
	// 		LOGGER.debug("process started");
	// 	} catch (IOException e) {

	// 		LOGGER.error(e.getMessage(), e);
	// 		return -1;
	// 	}

	// 	try {

	// 		final boolean exited = process.waitFor(20, TimeUnit.SECONDS);

	// 		if (!exited) {

	// 			LOGGER.error("process did not finish within 10 seconds");
	// 		}
	// 	} catch (final InterruptedException e) {

	// 		int exitCode = process.exitValue();
	// 		LOGGER.info(cmd.getShellScript() + " exited with exitCode code " + exitCode);

	// 		LOGGER.error(e.getMessage(), e);

	// 		return -2;
	// 	}

	// 	try {

	// 		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

	// 		String line;
	// 		StringBuffer sb = new StringBuffer();

	// 		while ((line = br.readLine()) != null) {

	// 			sb.append(line);
	// 		}

	// 		LOGGER.debug("process-output:");
	// 		LOGGER.debug(sb.toString());
	// 		LOGGER.debug("---");

	// 		int exitCode = process.exitValue();
	// 		LOGGER.info(cmd.getShellScript() + " exited with exitCode code " + exitCode);

	// 		if (exitCode != 0) {

	// 			LOGGER.error("Fehler bei {}: {} ", cmd.getShellScript(), sb.toString());
	// 			return exitCode;
	// 		}

	// 		return 0;
	// 	} catch (Exception e) {

	// 		LOGGER.error(e.getMessage(), e);

	// 		return -3;
	// 	}

	// }

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
