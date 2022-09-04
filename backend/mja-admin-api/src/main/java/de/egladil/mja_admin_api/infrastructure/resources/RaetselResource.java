// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EnumType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_admin_api.domain.dto.SortDirection;
import de.egladil.mja_admin_api.domain.dto.Suchfilter;
import de.egladil.mja_admin_api.domain.generatoren.RaetselGeneratorService;
import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.Raetsel;
import de.egladil.mja_admin_api.domain.raetsel.RaetselService;
import de.egladil.mja_admin_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_admin_api.domain.raetsel.dto.GeneratedImages;
import de.egladil.mja_admin_api.domain.raetsel.dto.GeneratedPDF;
import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_admin_api.domain.utils.DevDelayService;
import de.egladil.mja_admin_api.infrastructure.validation.CsrfTokenValidator;
import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.Session;

/**
 * RaetselResource
 */
@Path("/raetsel/v1")
public class RaetselResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselResource.class);

	@ConfigProperty(name = "authorization.enabled")
	boolean authorizationEnabled;

	@Context
	SecurityContext securityContext;

	@Inject
	DevDelayService delayService;

	@Inject
	RaetselService raetselService;

	@Inject
	RaetselGeneratorService generatorService;

	@Inject
	DeskriptorenService deskriptorenService;

	private final CsrfTokenValidator csrfTokenValidator = new CsrfTokenValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	// @formatter:off
	public RaetselsucheTreffer sucheRaetsel(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{4,30}$",
		message = "ungültige Eingabe: mindestens 4 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") final String suchstring,
		@QueryParam(value = "deskriptoren") @Pattern(
			regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
			message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren,
		@QueryParam(value = "typeDeskriptoren") @NotNull(message = "Angabe typeDeskriptoren ist erforderlich") final EnumType typeDeskriptoren,
		@QueryParam(value = "limit") @DefaultValue("10") final int limit,
		@QueryParam(value = "offset") @DefaultValue("0") final int offset,
		@QueryParam(value = "sortDirection")  @DefaultValue("asc") final SortDirection sortDirection) {
		// @formatter:on

		LOGGER.debug("authorizationEnabled=" + this.authorizationEnabled);

		this.delayService.pause();

		String deskriptorenOrdinal = checkAndTransformDeskriptoren(deskriptoren, typeDeskriptoren);

		if (StringUtils.isAllBlank(suchstring, deskriptorenOrdinal)) {

			return new RaetselsucheTreffer();
		}

		return raetselService.sucheRaetsel(new Suchfilter(suchstring, deskriptorenOrdinal), limit, offset,
			sortDirection);
	}

	@GET
	@Path("{raetselUuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response raetselDetailsLaden(@PathParam(value = "raetselUuid") final String raetselUuid) {

		this.delayService.pause();

		Raetsel raetsel = raetselService.getRaetselZuId(raetselUuid);

		if (raetsel == null) {

			return Response.status(404).build();
		}

		return Response.status(200).entity(raetsel).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response raetselAnlegen(final EditRaetselPayload payload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		this.delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		Raetsel raetsel = raetselService.raetselAnlegen(payload, userUuid);
		return Response.status(201).entity(raetsel).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response raetselAendern(final EditRaetselPayload payload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		this.delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		Raetsel raetsel = raetselService.raetselAendern(payload, userUuid);
		return Response.status(200).entity(raetsel).build();
	}

	@GET
	@Path("PNG/{raetselUuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public GeneratedImages raetselImagesGenerieren(@PathParam(
		value = "raetselUuid") final String raetselUuid, @QueryParam(
			value = "layoutAntwortvorschlaege") final LayoutAntwortvorschlaege layoutAntwortvorschlaege, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		this.delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";

		GeneratedImages result = generatorService.generatePNGsRaetsel(raetselUuid, layoutAntwortvorschlaege);

		LOGGER.info("Raetsel Images generiert: [raetsel={}, user={}]", raetselUuid, StringUtils.abbreviate(userUuid, 11));

		return result;
	}

	@GET
	@Path("PDF/{raetselUuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "ADMIN", "LEHRER", "PRIVAT", "STANDARD" })
	public GeneratedPDF raetselPDFGenerieren(@PathParam(
		value = "raetselUuid") final String raetselUuid, @QueryParam(
			value = "layoutAntwortvorschlaege") final LayoutAntwortvorschlaege layoutAntwortvorschlaege, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		this.delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";

		GeneratedPDF result = generatorService.generatePDFRaetsel(raetselUuid, layoutAntwortvorschlaege);

		LOGGER.info("Raetsel PDF generiert: [raetsel={}, user={}]", raetselUuid, StringUtils.abbreviate(userUuid, 11));

		return result;
	}

	/**
	 * @param  deskriptoren
	 * @param  typeDeskriptoren
	 * @return
	 */
	String checkAndTransformDeskriptoren(final String deskriptoren, final EnumType typeDeskriptoren) {

		String deskriptorenOrdinal = deskriptoren;

		if (StringUtils.isNotBlank(deskriptoren) && EnumType.STRING == typeDeskriptoren) {

			deskriptorenOrdinal = deskriptorenService.transformToDeskriptorenOrdinal(deskriptoren);

		}
		return deskriptorenOrdinal;
	}

}
