// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.AbstractDomainEntity;
import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_admin_api.domain.sammlungen.dto.EditRaetselsammlungPayload;
import de.egladil.mja_admin_api.domain.sammlungen.impl.RaetselsammlungService;
import de.egladil.mja_admin_api.domain.utils.DevDelayService;
import de.egladil.mja_admin_api.infrastructure.validation.CsrfTokenValidator;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.Session;

/**
 * RaetselsammlungenResource
 */
@Path("raetselsammlungen/v1")
@Produces(MediaType.APPLICATION_JSON)
public class RaetselsammlungenResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselsammlungenResource.class);

	@ConfigProperty(name = "authorization.enabled")
	boolean authorizationEnabled;

	@Context
	SecurityContext securityContext;

	@Inject
	DevDelayService delayService;

	@Inject
	RaetselsammlungService raetselsammlungService;

	private final CsrfTokenValidator csrfTokenValidator = new CsrfTokenValidator();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response raetselsammlungAnlegen(final EditRaetselsammlungPayload requestPayload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		if (!AbstractDomainEntity.UUID_NEUE_ENTITY.equals(requestPayload.getId())) {

			LOGGER.error("POST mit vorhandener Entity uuid={} aufgerufen", requestPayload.getId());
			return Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("POST darf nur mit id='neu' aufgerufen werden"))
				.build();
		}

		RaetselsucheTrefferItem raetselsammlung = raetselsammlungService.raetselsammlungAnlegen(requestPayload, userUuid);

		LOGGER.info("Raetselsammlung angelegt: [raetselsammlung={}, user={}]", raetselsammlung.getId(),
			StringUtils.abbreviate(userUuid, 11));

		return Response.status(201).entity(raetselsammlung).build();
	}

}
