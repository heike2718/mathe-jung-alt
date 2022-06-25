// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_auth.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * ResponseFilter
 */
@Provider
public class ResponseFilter implements ContainerResponseFilter {

	private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

		final MultivaluedMap<String, Object> headers = responseContext.getHeaders();

		if (headers.get(CONTENT_SECURITY_POLICY) == null) {

			responseContext.getHeaders().add(CONTENT_SECURITY_POLICY, "default-src 'self'; ");
		}

		if (headers.get("Access-Control-Allow-Headers") == null) {

			headers.add("Access-Control-Allow-Headers",
				"Content-Type,Accept,X-Requested-With,Content-Disposition,X-SESSIONID,X-XSRF-TOKEN");
		}

		if (headers.get("Access-Control-Expose-Headers") == null) {

			headers.add("Access-Control-Expose-Headers",
				"Content-Type,Content-Disposition,X-SESSIONID,X-XSRF-TOKEN");
		}
	}
}
