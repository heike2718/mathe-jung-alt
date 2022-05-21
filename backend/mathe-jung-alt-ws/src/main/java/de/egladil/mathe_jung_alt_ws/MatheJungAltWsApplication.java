// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

/**
 * MatheJungAltWsApplication
 */
@OpenAPIDefinition(
	info = @Info(
		title = "Mathe-Jung-Alt - API",
		version = "1.0.0",
		contact = @Contact(
			name = "Mathe-Jung-Alt - API",
			url = "https://mathe-jung-alt.de/impressum.html",
			email = "mathe@egladil.de"),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
public class MatheJungAltWsApplication extends Application {

	// absichtlich leer
}
