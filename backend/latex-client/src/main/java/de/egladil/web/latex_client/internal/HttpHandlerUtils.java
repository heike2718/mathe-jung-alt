// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_client.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;

import de.egladil.web.latex_client.LaTeXCommand;
import de.egladil.web.latex_client.exception.InvalidInputException;
import de.egladil.web.latex_client.exception.LaTeXClientException;
import io.undertow.server.HttpServerExchange;

/**
 * HttpHandlerUtils
 */
public class HttpHandlerUtils {

	private static final Logger LOGGER = Logger.getLogger(HttpHandlerUtils.class);

	public LaTeXCommand getCommand(final HttpServerExchange exchange) {

		String relativePath = exchange.getRelativePath();
		LOGGER.debug("relativePath=" + relativePath);

		LaTeXCommand cmd = LaTeXCommand.fromRelativePath(relativePath);

		return cmd;
	}

	public String getFileName(final HttpServerExchange exchange) throws LaTeXClientException {

		try {

			Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();

			String filename = null;

			if (queryParameters.isEmpty() || queryParameters.size() > 1) {

				throw new InvalidInputException(Collections.singletonList("query parameter"),
					"es ist nur ein Query-Parameter namens 'filename' erlaubt");
			}

			Optional<String> optKey = queryParameters.keySet().stream().filter(k -> "filename".equals(k)).findFirst();

			if (optKey.isEmpty()) {

				List<String> invalidInputs = new ArrayList<>(queryParameters.keySet());

				throw new InvalidInputException(invalidInputs, "es ist nur ein Query-Parameter namens 'filename' erlaubt");
			}

			filename = queryParameters.get("filename").getFirst();

			return filename;

		} catch (InvalidInputException e) {

			throw e;
		} catch (Exception e) {

			throw new LaTeXClientException("Exception beim ermitteln des Query-Parameters 'filename': " + e.getMessage(), e);

		}
	}

}
