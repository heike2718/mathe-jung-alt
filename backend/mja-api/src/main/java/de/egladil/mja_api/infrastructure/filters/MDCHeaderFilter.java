// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MDCHeaderFilter
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class MDCHeaderFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MDCHeaderFilter.class);

	private static final String X_CORRELATION_ID_HEADER_NAME = "X-CORRELATION-ID";

	private static final String X_CLIENT_ID_HEADER_NAME = "X-CLIENT-ID";

	private static final String MDC_KEY_USER_ID = "userId";

	private static final String MDC_KEY_CLIENT_ID = "clientId";

	private static final String MDC_KEY_CORRELATION_ID = "correlationId";

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		String method = requestContext.getMethod();

		if (!"OPTIONS".equals(method)) {

			String path = requestContext.getUriInfo().getPath();
			LOGGER.info("request.path={}", path);

			String correlationId = getOrCreateCorrelationId(requestContext);
			String clientId = getClientId(requestContext);

			MDC.put(MDC_KEY_CORRELATION_ID, correlationId);
			MDC.put(MDC_KEY_CLIENT_ID, clientId);
		}

	}

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

		MDC.remove(MDC_KEY_CORRELATION_ID);
		MDC.remove(MDC_KEY_CLIENT_ID);
		MDC.remove(MDC_KEY_USER_ID);

	}

	String getOrCreateCorrelationId(final ContainerRequestContext ctx) {

		String correlationId = ctx.getHeaderString(X_CORRELATION_ID_HEADER_NAME);
		return correlationId != null ? correlationId : UUID.randomUUID().toString();

	}

	String getClientId(final ContainerRequestContext ctx) {

		String clientId = ctx.getHeaderString(X_CLIENT_ID_HEADER_NAME);
		return clientId != null ? clientId : "unknown";
	}

}
