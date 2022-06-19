// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

/**
 * MjaAdminApiApplication
 */
@OpenAPIDefinition(
	info = @Info(
		title = "Mathe-Jung-Alt - Admin-API",
		version = "1.0.0",
		contact = @Contact(
			name = "Mathe-Jung-Alt - Admin-API",
			url = "https://mathe-jung-alt.de/index.html",
			email = "mathe@egladil.de"),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
public class MjaAdminApiApplication extends Application {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final String STAGE_DEV = "dev";
}
