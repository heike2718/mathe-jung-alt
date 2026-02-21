// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.auth.login;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

import de.egladil.raetselbaukasten.domain.auth.clientauth.OAuthClientCredentialsProvider;
import de.egladil.raetselbaukasten.domain.auth.config.ConfigService;
import de.egladil.raetselbaukasten.domain.auth.dto.AuthResult;
import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.auth.dto.OAuthClientCredentials;
import de.egladil.raetselbaukasten.domain.auth.session.Session;
import de.egladil.raetselbaukasten.domain.auth.session.SessionService;
import de.egladil.raetselbaukasten.domain.auth.session.SessionUtils;
import de.egladil.raetselbaukasten.domain.auth.util.CsrfCookieService;

/**
 * LoginLogoutService
 */
@RequestScoped
public class LoginLogoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutService.class);

    @ConfigProperty(name = "cookies.secure")
    boolean cookiesSecure;

    @Inject
    OAuthClientCredentialsProvider clientCredentialsProvider;

    @Inject
    SessionService sessionService;

    @Inject
    TokenExchangeService tokenExchangeService;

    @Inject
    ConfigService configService;

    @Inject
    CsrfCookieService csrfCookieService;

    public Response login(final AuthResult authResult) {

        if (authResult == null) {

            LOGGER.warn("login wurde ohne payload aufgerufen");

            try (Response response = Response
                    .status(Status.BAD_REQUEST)
                    .entity(MessagePayload.error("login: erwarte authResult"))
                    .build()) {
                throw new WebApplicationException(response);

            }
        }

        String oneTimeToken = authResult.getIdToken();

        LOGGER.debug("idToken={}", StringUtils.abbreviate(oneTimeToken, 11));

        OAuthClientCredentials clientCredentials = clientCredentialsProvider.getClientCredentials(null);

        String jwt = this.tokenExchangeService
                .exchangeTheOneTimeToken(clientCredentials.getClientId(), clientCredentials.getClientSecret(),
                        oneTimeToken);

        Session session = this.sessionService.initSession(jwt);

        if (session.isAnonym()) {

            session.clearSessionIdInProd();
            return Response
                    .status(Status.FORBIDDEN)
                    .entity(MessagePayload
                            .error("Sie haben leider keine Berechtigung, sich in die Administration einzuloggen."))
                    .build();
        }

        NewCookie sessionCookie = SessionUtils.createSessionCookie(session.getSessionId(), cookiesSecure);

        if (!ConfigService.STAGE_DEV.equals(configService.getStage())) {

            session.clearSessionIdInProd();
        }

        return Response.ok(session).cookie(csrfCookieService.createCsrfTokenCookie()).cookie(sessionCookie).build();
    }

    public Response logout(final String sessionId) {

        this.sessionService.invalidateSession(sessionId);

        NewCookie invalidatedSessionCookie = SessionUtils.createSessionInvalidatedCookie(cookiesSecure);

        return Response
                .ok(MessagePayload.info("erfolgreich ausgeloggt"))
                .cookie(csrfCookieService.createCsrfTokenCookie())
                .cookie(invalidatedSessionCookie)
                .build();
    }

    public Response logoutDev(final String sessionId) {

        this.sessionService.invalidateSession(sessionId);

        if (!ConfigService.STAGE_DEV.equals(configService.getStage())) {

            LOGGER.warn("stage={}" + configService.getStage());
            return Response
                    .status(401)
                    .entity(MessagePayload.error("böse böse. Dieser Request wurde geloggt!"))
                    .cookie(SessionUtils.createSessionInvalidatedCookie(cookiesSecure))
                    .build();
        }

        return Response.ok(MessagePayload.info("erfolgreich ausgeloggt")).build();
    }
}
