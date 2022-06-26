// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_api;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

/**
 * MjaApiApplication
 */
@OpenAPIDefinition(
	info = @Info(
		title = "Mathe-Jung-Alt - API",
		version = "1.0.0",
		contact = @Contact(
			name = "Mathe für jung und alt",
			url = "https://mathe-jung-alt.de/index.html",
			email = "mathe@egladil.de"),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
public class MjaApiApplication extends Application {

	// absichtlich leer

}
