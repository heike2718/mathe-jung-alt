// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.MjaApiApplication;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.s2s.ClientAuthService;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * BasicAuthenticationFilter
 */
@ApplicationScoped
@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class BasicAuthenticationFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

	private final RestrictedRequestsDelegate restrictedPathsDelegate = new RestrictedRequestsDelegate();

	@Inject
	ClientAuthService authService;

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		LOGGER.debug("entering Filter");

		if (!restrictedPathsDelegate.needsS2SAuthorization(requestContext)) {

			return;
		}

		String authorizationHeader = requestContext.getHeaderString("Authorization");
		String path = requestContext.getUriInfo().getPath();

		if (authorizationHeader == null) {

			LOGGER.info("Aufruf {} ohne Authorization-Header. AuthorizationHeader ist erforderlich!", path);
			throw new WebApplicationException(
				Response.status(400)
					.entity(MessagePayload.error("S2S-Authentifizierung fehlgeschlagen. Authorization-Header ist erforderlich."))
					.build());
		}

		LOGGER.debug("AuthorizationHeader={}", StringUtils.abbreviate(authorizationHeader, 11));

		String clientId = getClientId(requestContext);

		Pair<String, Boolean> authResult = authService.authorize(authorizationHeader);

		if (!authResult.getRight().booleanValue()) {

			throw new WebApplicationException(Response.status(401).entity(MessagePayload.error(
				"keine Berechtigung: S2S-Authentifizierung fehlgeschlagen. Bitte konfigurierten Authorization-Header pruefen."))
				.build());
		} else {

			if (!clientId.equals(authResult.getLeft())) {

				LOGGER.warn("clientId aus Authorization-Header und {} stimmen nicht überein: [clientId={}, {}={}]",
					MjaApiApplication.X_CLIENT_ID_HEADER_NAME, authResult.getLeft(), MjaApiApplication.X_CLIENT_ID_HEADER_NAME,
					clientId);

				throw new WebApplicationException(Response.status(401).entity(MessagePayload.error(
					"keine Berechtigung: S2S-Authentifizierung fehlgeschlagen. Bitte Konfiguration von mk-gateway.auth.client und Header X-CLIENT-ID pruefen."))
					.build());
			}
			LOGGER.info("path={}", path);
		}
	}

	String getClientId(final ContainerRequestContext ctx) {

		String clientId = ctx.getHeaderString(MjaApiApplication.X_CLIENT_ID_HEADER_NAME);
		return clientId != null ? clientId : "unknown";
	}
}
