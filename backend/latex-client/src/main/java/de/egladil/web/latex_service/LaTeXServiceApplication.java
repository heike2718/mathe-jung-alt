// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_service;

import de.egladil.web.latex_service.internal.CustomHttpHandler;
import io.undertow.Undertow;

/**
 * LaTeXServiceApplication
 */
public class LaTeXServiceApplication {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		Undertow server = Undertow.builder()
			// Set up the listener - you can change the port/host here
			.addHttpListener(8080, "0.0.0.0")
			.setHandler(new CustomHttpHandler()).build();

		// Boot the web server
		server.start();

		System.out.println("========================================================");
		System.out.println("LaTeXServiceApplication started - listening to port 8080");
		System.out.println("========================================================");

	}

}
