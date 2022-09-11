// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenService;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenSuchparameter;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.raetselgruppen.dto.EditRaetselgruppePayload;
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
@Produces(MediaType.APPLICATION_JSON)
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
	// @formatter:off
	public RaetselgruppensucheTreffer findGruppen(
		@QueryParam(value = "name") @Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*", message = "name enthält unerlaubte Zeichen")
		@Size(min = 1, max = 100, message = "nicht mehr als 100 Zeichen") final String name,
		@QueryParam(value = "schwierigkeitsgrad") final Schwierigkeitsgrad schwierigkeitsgrad,
		@QueryParam(value = "referenztyp") final Referenztyp referenztyp,
		@QueryParam(value = "referenz") @Pattern(regexp = "^[\\w äöüß]{1,20}$" , message = "referenz enthält unerlaubte Zeichen")
		@Size(min = 1, max = 36, message = "nicht mehr als 36 Zeichen") final String referenz,
//		@QueryParam(value = "sortName") @NotNull final SortDirection sortDirectionName,
//		@QueryParam(value = "sortSchwierigkeitsgrad") @NotNull final SortDirection sortDirectionSchwierigkeitsgrad,
//		@QueryParam(value = "sortReferenztyp") @NotNull final SortDirection sortDirectionReferenztyp,
//		@QueryParam(value = "sortReferenz") @NotNull final SortDirection sortDirectionReferenz,
		@QueryParam(value = "limit") @DefaultValue("20") final int limit,
		@QueryParam(value = "offset") @DefaultValue("0") final int offset) {
		// @formatter:off

		if (limit > 50) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("nicht mehr als 50 auf einmal abfragen.")).build());
		}

//		RaetselgruppenSuchparameter suchparameter = new RaetselgruppenSuchparameter(name, schwierigkeitsgrad, referenztyp, referenz, sortDirectionName, sortDirectionSchwierigkeitsgrad, sortDirectionReferenztyp, sortDirectionReferenz);

		RaetselgruppenSuchparameter suchparameter = new RaetselgruppenSuchparameter(name, schwierigkeitsgrad, referenztyp, referenz);
		return raetselgruppenService.findRaetselgruppen(suchparameter, limit, offset);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response raetselsammlungAnlegen(final EditRaetselgruppePayload requestPayload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		RaetselgruppensucheTrefferItem raetselsammlung = raetselgruppenService.raetselgruppeAnlegen(requestPayload, userUuid);

		LOGGER.info("Raetselsammlung angelegt: [raetselsammlung={}, user={}]", raetselsammlung.getId(),
			StringUtils.abbreviate(userUuid, 11));

		return Response.status(201).entity(raetselsammlung).build();
	}

}
