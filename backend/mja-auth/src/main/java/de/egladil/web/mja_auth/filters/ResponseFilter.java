// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import de.egladil.web.mja_auth.config.ConfigService;

/**
 * ResponseFilter
 */
@Provider
public class ResponseFilter implements ContainerResponseFilter {

	private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

	@Inject
	ConfigService configService;

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

		final MultivaluedMap<String, Object> headers = responseContext.getHeaders();

		if (headers.get(CONTENT_SECURITY_POLICY) == null) {

			responseContext.getHeaders().add(CONTENT_SECURITY_POLICY, "default-src 'self'; ");
		}

		if (headers.get("Access-Control-Allow-Headers") == null) {

			headers.add("Access-Control-Allow-Headers",
				configService.getAllowedHeaders());
		}

		if (headers.get("Access-Control-Expose-Headers") == null) {

			headers.add("Access-Control-Expose-Headers",
				configService.getExposedHeaders());
		}

		if (headers.get("Access-Control-Allow-Origin") == null) {

			headers.add("Access-Control-Allow-Origin", configService.getAllowedOrigin());
		}

		if (headers.get("Access-Control-Allow-Credentials") == null) {

			headers.add("Access-Control-Allow-Credentials", "false");
		}

		// Achtung: mod-security verbietet standardmäßig PUT und DELETE.
		// Daher parallel in /etc/apache2/sites-available/opa-wetterwachs.conf die rule 911100 für checklistenserver entfernen,
		// sonst bekommt man 403
		if (headers.get("Access-Control-Allow-Methods") == null) {

			headers.add("Access-Control-Allow-Methods", configService.getAllowedMethods());
		}
	}
}
