// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * CustomHttpHandler
 */
public class CustomHttpHandler implements HttpHandler {

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {

		String relativePath = exchange.getRelativePath();
		System.out.println("relativePath=" + relativePath);

		LaTeXCommand cmd = LaTeXCommand.fromRelativePath(relativePath);

		StringBuilder sb = new StringBuilder();

		if (cmd != null) {

			Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();

			Deque<String> dd = queryParameters.get("filename");
			String filename = dd.getFirst();

			if (filename == null) {

				filename = "02819";
			}

			sb.append("execute command ");
			sb.append(cmd);
			sb.append(" on file ");
			sb.append(filename);
			sb.append("\n");

			int exitCode = this.performCommand(cmd, filename);
			sb.append("exitCode=");
			sb.append(exitCode);
		}

		exchange.getResponseHeaders()
			.put(Headers.CONTENT_TYPE, "text/plain");

		exchange.getResponseSender()
			.send("fertig: " + sb.toString());
	}

	private int performCommand(final LaTeXCommand cmd, final String fileName) {

		List<String> cmdList = new ArrayList<String>();
		cmdList.add("sh");
		cmdList.add(cmd.getShellScript());
		cmdList.add(fileName);

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.redirectErrorStream(true);
		processBuilder.command(cmdList);

		try {

			Process process = processBuilder.start();
			System.out.println("process started");

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();

			while ((line = br.readLine()) != null) {

				sb.append(line);
			}

			System.out.println("process-output:");
			System.out.println(sb.toString());
			System.out.println("---");
			;

			int exitCode = process.waitFor();

			System.out.println("call-docker-pdf exited with exitCode code " + exitCode);

			if (exitCode != 0) {

				System.err.println("Fehler beim Process: " + sb.toString());
				return exitCode;
			}

			return 0;
		} catch (IOException e) {

			e.printStackTrace();
			return -1;

		} catch (InterruptedException e) {

			e.printStackTrace();

			return -2;
		}

	}
}
