// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.annotation.security.PermitAll;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import de.egladil.mja_api.domain.auth.ClientType;
import de.egladil.mja_api.domain.auth.dto.AuthResult;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.login.LoginLogoutService;
import de.egladil.mja_api.domain.auth.login.LoginUrlService;
import de.egladil.mja_api.domain.auth.session.SessionUtils;
import de.egladil.mja_api.domain.utils.DevDelayService;

/**
 * SessionResource
 */
@RequestScoped
@Path("mja-api/session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Session")
public class SessionResource {

	@Inject
	DevDelayService delayService;

	@Inject
	LoginUrlService loginUrlService;

	@Inject
	LoginLogoutService loginLogoutService;

	@GET
	@Path("authurls/login/{clientType}")
	@PermitAll
	@Operation(
		operationId = "getLoginUrl",
		summary = "Gibt die Login-URL zurück, mit der eine Anwendung zum authprovider redirecten kann")
	@Parameters({
		@Parameter(
			name = "clientType",
			description = "Typ des Clients beim authprovider") })
	@APIResponse(
		name = "GetLoginUrlOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response getLoginUrl(@PathParam(value = "clientType") final ClientType clientType) {

		this.delayService.pause();

		return this.loginUrlService.getLoginUrl(clientType);

	}

	@POST
	@Path("login/{clientType}")
	@PermitAll
	@Operation(
		operationId = "login",
		summary = "Erzeugt eine Session anhand des per S2S-Kommunikation für das 'one time token' beim authprovider gekauften JWT und packt die SessionId in ein Cookie")
	@Parameters({
		@Parameter(
			name = "clientType",
			description = "Typ des Clients beim authprovider"),
		@Parameter(
			name = "authResult",
			description = "das Resultat der Authentifizierung beim authprovider") })
	@APIResponse(
		name = "GetLoginUrlOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response login(@PathParam(
		value = "clientType") final ClientType clientType, final AuthResult authResult) {

		this.delayService.pause();

		return loginLogoutService.login(clientType, authResult, clientType == ClientType.ADMIN);
	}

	@DELETE
	@Path("logout")
	@PermitAll
	@Operation(
		operationId = "logout",
		summary = "entfernt die Session")
	@Parameters({
		@Parameter(
			name = "sessionId",
			description = "id der Session (cookie)"),
	})
	@APIResponse(
		name = "GetLoginUrlOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response logout(@CookieParam(
		value = SessionUtils.SESSION_COOKIE_NAME) final String sessionId) {

		return loginLogoutService.logout(sessionId);
	}

	@DELETE
	@Path("dev/logout/{sessionId}")
	@PermitAll
	@Operation(
		operationId = "logoutDev",
		summary = "entfernt die Session (nur Dev)")
	@Parameters({
		@Parameter(
			name = "sessionId",
			description = "id der Session"),
	})
	@APIResponse(
		name = "GetLoginUrlOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response logoutDev(@PathParam(value = "sessionId") final String sessionId) {

		return loginLogoutService.logoutDev(sessionId);
	}

}
