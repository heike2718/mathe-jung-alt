// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.config.ConfigService;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Session;
import de.egladil.mja_api.domain.auth.session.SessionService;
import de.egladil.mja_api.domain.auth.session.SessionUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContextImpl;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

/**
 * InitSecurityContextFilter
 */
@ApplicationScoped
@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class InitSecurityContextFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitSecurityContextFilter.class);

	private static List<String> OPEN_DATA_PATHS = Arrays.asList(new String[] { "/mja-api/quiz" });

	@Inject
	ConfigService configService;

	@Inject
	SessionService sessionService;

	@Inject
	AuthenticationContextImpl authCtx; // Injecting the implementation, not the interface!!!

	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		// https://quarkus.io/guides/context-propagation

		String method = requestContext.getMethod();

		if ("OPTIONS".equals(method)) {

			LOGGER.debug("keine Auth bei OPTIONS");

			return;
		}

		String path = requestContext.getUriInfo().getPath();

		boolean openData = this.isPathOpenData(path);

		LOGGER.info("stage={}, mockSession={}, path={}, openData={}", configService.getStage(), configService.isMockSession(), path,
			openData);

		if (openData) {

			this.addUserToAuthAndSecurityContext(AuthenticatedUser.createAnonymousUser(), requestContext);
			return;
		}

		if (!ConfigService.STAGE_PROD.equals(configService.getStage()) && configService.isMockSession()) {

			LOGGER.warn("Achtung: mock-Session!!! check properties 'stage' und 'mock.session' [stage={}, mockSession=",
				configService.getStage(), configService.isMockSession());

			initMockSecurityContext(requestContext);
		} else {

			LOGGER.debug("path={}", path);

			String sessionId = SessionUtils.getSessionId(requestContext, configService.getStage());

			LOGGER.debug("sessionId={}", sessionId);

			if (sessionId != null) {

				Session session = sessionService.getAndRefreshSessionIfValid(sessionId);

				if (session != null) {

					AuthenticatedUser user = session.getUser();

					if (user != null) {

						addUserToAuthAndSecurityContext(user, requestContext);
					} else {

						LOGGER.warn("path={}, user ist null, die Anwendung wird nicht funktionieren!", path);
					}

				}
			}

		}
	}

	/**
	 * @param requestContext
	 */
	private void addUserToAuthAndSecurityContext(final AuthenticatedUser user, final ContainerRequestContext requestContext) {

		authCtx.setUser(user);

		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public boolean isUserInRole(final String role) {

				Optional<String> opt = Arrays.stream(user.getRoles()).filter(r -> role.equalsIgnoreCase(r)).findFirst();
				return opt.isPresent();
			}

			@Override
			public boolean isSecure() {

				return true;
			}

			@Override
			public Principal getUserPrincipal() {

				return new Principal() {

					@Override
					public String getName() {

						return user.getUuid();
					}
				};
			}

			@Override
			public String getAuthenticationScheme() {

				return null;
			}
		});

		LOGGER.debug("user {} added to AuthenticationContext and SecurityContext", user);

	}

	private void initMockSecurityContext(final ContainerRequestContext requestContext) {

		AuthenticatedUser user = new AuthenticatedUser("b865fc75-1bcf-40c7-96c3-33744826e49f").withFullName("Heike Winkelvoß")
			.withIdReference("bla").withRoles(new String[] { "ADMIN" });

		authCtx.setUser(user);
		LOGGER.warn("config property 'mock.session' is true => authCtx with mocked user: ");
	}

	boolean isPathOpenData(final String path) {

		return OPEN_DATA_PATHS.stream().filter(p -> path.startsWith(p)).findFirst().isPresent();

	}
}
