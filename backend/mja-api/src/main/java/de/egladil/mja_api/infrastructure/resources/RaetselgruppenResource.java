// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenService;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSortattribute;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSuchparameter;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppenelementPayload;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppeDetails;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTreffer;
import de.egladil.mja_api.domain.raetselgruppen.dto.RaetselgruppensucheTrefferItem;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.mja_api.infrastructure.validation.CsrfTokenValidator;
import de.egladil.web.commons_validation.payload.MessagePayload;
import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.Session;

/**
 * RaetselgruppenResource
 */
@Path("raetselgruppen/v1")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Raetselgruppen")
public class RaetselgruppenResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselgruppenResource.class);

	@ConfigProperty(name = "authorization.enabled")
	boolean authorizationEnabled;

	@Context
	SecurityContext securityContext;

	@Inject
	DevDelayService delayService;

	@Inject
	RaetselgruppenService raetselgruppenService;

	private final CsrfTokenValidator csrfTokenValidator = new CsrfTokenValidator();

	@GET
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "findGruppen", summary = "Gibt alle Rätselgruppen zurück, die auf die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			name = "name",
			description = "Teil des Namens der Gruppe (Suche mit like)"),
		@Parameter(
			name = "schwierigkeitsgrad",
			description = "Klassenstufe, für die die Rätselgruppe gedacht ist (enum)"),
		@Parameter(
			name = "referenztyp",
			description = "Kontext zur Interpretation des Parameters 'referenz'"),
		@Parameter(
			name = "referenz",
			description = "ID im alten Aufgabenarchiv"),
		@Parameter(
			name = "sortAttribute",
			description = "Attribut, nach dem sortiert wird"),
		@Parameter(
			name = "sortDirection",
			description = "Sortierrichtung für das gewählte Attribut"),
		@Parameter(
			name = "limit",
			description = "Pagination: pageSize"),
		@Parameter(
			name = "offset",
			description = "Pagination: pageIndex") })
	@APIResponse(
		name = "FindRaetselgruppenOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = RaetselgruppensucheTreffer.class)))
	// @formatter:off
	public RaetselgruppensucheTreffer findGruppen(
		@QueryParam(value = "name") @Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*", message = "name enthält unerlaubte Zeichen")
		@Size(min = 1, max = 100, message = "nicht mehr als 100 Zeichen") final String name,
		@QueryParam(value = "schwierigkeitsgrad") final Schwierigkeitsgrad schwierigkeitsgrad,
		@QueryParam(value = "referenztyp") final Referenztyp referenztyp,
		@QueryParam(value = "referenz") @Pattern(regexp = "^[\\w äöüß]{1,20}$" , message = "referenz enthält unerlaubte Zeichen")
		@Size(min = 1, max = 36, message = "nicht mehr als 36 Zeichen") final String referenz,
		@QueryParam(value = "sortAttribute") @DefaultValue("name") final RaetselgruppenSortattribute sortAttribute,
		@QueryParam(value = "sortDirection") @DefaultValue("asc")  final SortDirection sortDirection,
		@QueryParam(value = "limit") @DefaultValue("20") final int limit,
		@QueryParam(value = "offset") @DefaultValue("0") final int offset) {
		// @formatter:off

		if (limit > 50) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("nicht mehr als 50 auf einmal abfragen.")).build());
		}

		RaetselgruppenSuchparameter suchparameter = new RaetselgruppenSuchparameter(name, schwierigkeitsgrad, referenztyp, referenz, sortAttribute, sortDirection);
		return raetselgruppenService.findRaetselgruppen(suchparameter, limit, offset);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselgruppeAnlegen",
		summary = "neue Rätselgruppe anlegen")
	@APIResponse(
		name = "CreateRaetselgruppeOKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = RaetselgruppensucheTrefferItem.class)))
	@APIResponse(
		name = "CreateRaetselgruppeServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response raetselgruppeAnlegen(final EditRaetselgruppePayload requestPayload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		RaetselgruppensucheTrefferItem raetselsammlung = raetselgruppenService.raetselgruppeAnlegen(requestPayload, userUuid);

		LOGGER.info("Raetselgruppe angelegt: [raetselgruppe={}, user={}]", raetselsammlung.getId(),
			StringUtils.abbreviate(userUuid, 11));

		return Response.status(201).entity(raetselsammlung).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselgruppeAendern",
		summary = "neue Rätselgruppe anlegen")
	@APIResponse(
		name = "UpdateRaetselgruppeOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = RaetselgruppensucheTrefferItem.class)))
	@APIResponse(
		name = "UpdateRaetselgruppeServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response raetselgruppeAendern(final EditRaetselgruppePayload requestPayload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		RaetselgruppensucheTrefferItem raetselsammlung = raetselgruppenService.raetselgruppeBasisdatenAendern(requestPayload, userUuid);

		LOGGER.info("Raetselgruppe angelegt: [raetselgruppe={}, user={}]", raetselsammlung.getId(),
			StringUtils.abbreviate(userUuid, 11));

		return Response.status(200).entity(raetselsammlung).build();
	}

	@GET
	@Path("{raetselgruppeID}")
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselgruppeDetailsLaden",
		summary = "Läd die Details der Rätselgruppe mit der gegebenen ID")
	@Parameters({
		@Parameter(
			name = "raetselgruppeID",
			description = "technische ID der Rätselgruppe") })
	@APIResponse(
		name = "FindRaetselgruppeByIDOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = RaetselgruppeDetails.class)))
	@APIResponse(
		name = "RaetselgruppeByIDNotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public RaetselgruppeDetails raetselgruppeDetailsLaden(@PathParam(value = "raetselgruppeID") @Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$",
		message = "raetselgruppeID enthält ungültige Zeichen") final String raetselgruppeID) {

		Optional<RaetselgruppeDetails> optDetails = raetselgruppenService.loadDetails(raetselgruppeID);

		if (optDetails.isEmpty()) {
			throw new WebApplicationException(Response.status(404).entity(MessagePayload.error("kein Treffer")).build());
		}

		return optDetails.get();
	}

	@POST
	@Path("{raetselgruppeID}/elemente")
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselgruppenelementAnlegen",
		summary = "Legt ein neues Element in einer Rätselgruppe an")
	@Parameters({
		@Parameter(
			name = "raetselgruppeID",
			description = "technische ID der Rätselgruppe")})
	@APIResponse(
		name = "CreateRaetselgruppenelementOKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = RaetselgruppeDetails.class)))
	@APIResponse(
		name = "CreateRaetselgruppenelementNotFoundResponse",
		description = "Die Rätselgruppe oder das Rätsel mit dem fachlichen SCHLUESSEL gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "CreateRaetselgruppenelementConflict",
		description = "Nummer würde doppelt vergeben",
		responseCode = "409", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "CreateRaetselgruppenelementServerError",
		description = "Serverfehler",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public RaetselgruppeDetails raetselgruppenelementAnlegen(@PathParam(value = "raetselgruppeID") @Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$",
		message = "raetselgruppeID enthält ungültige Zeichen") final String raetselgruppeID, final EditRaetselgruppenelementPayload element) {

		return this.raetselgruppenService.elementAnlegen(raetselgruppeID, element);
	}


	@PUT
	@Path("{raetselgruppeID}/elemente")
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselgruppenelementAendern",
		summary = "Ändert das Element einer Rätselgruppe")
	@Parameters({
		@Parameter(
			name = "raetselgruppeID",
			description = "technische ID der Rätselgruppe"),
		@Parameter(
			name = "elementID",
			description = "technische ID des Elements") })
	@APIResponse(
		name = "UpdateRaetselgruppenelementOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = RaetselgruppeDetails.class)))
	@APIResponse(
		name = "UpdateRaetselgruppenelementNotFoundResponse",
		description = "Die Rätselgruppe oder das Element gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "UpdateRaetselgruppenelementServerError",
		description = "Gibt es nicht",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public RaetselgruppeDetails raetselgruppenelementAendern(@PathParam(value = "raetselgruppeID") @Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$",
		message = "raetselgruppeID enthält ungültige Zeichen") final String raetselgruppeID, final EditRaetselgruppenelementPayload element) {

		return this.raetselgruppenService.elementAendern(raetselgruppeID, element);
	}

	@DELETE
	@Path("{raetselgruppeID}/elemente/{elementID}")
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselgruppenelementLoeschen",
		summary = "Löscht das Element einer Rätselgruppe")
	@Parameters({
		@Parameter(
			name = "raetselgruppeID",
			description = "technische ID der Rätselgruppe"),
		@Parameter(
			name = "elementID",
			description = "technische ID des Elements") })
	@APIResponse(
		name = "DeleteRaetselgruppenelementOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = RaetselgruppeDetails.class)))
	@APIResponse(
		name = "DeleteRaetselgruppenelementNotFoundResponse",
		description = "Die Rätselgruppe oder das Element gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "DeleteRaetselgruppenelementServerError",
		description = "Gibt es nicht",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public RaetselgruppeDetails raetselgruppenelementLoeschen(@PathParam(value = "raetselgruppeID") @Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$",
		message = "raetselgruppeID enthält ungültige Zeichen") final String raetselgruppeID, @PathParam(value = "elementID") @Pattern(
			regexp = "^[a-fA-F\\d\\-]{1,36}$",
			message = "raetselgruppeID enthält ungültige Zeichen") final String elementID) {

		return raetselgruppenService.elementLoeschen(raetselgruppeID, elementID);
	}
}
