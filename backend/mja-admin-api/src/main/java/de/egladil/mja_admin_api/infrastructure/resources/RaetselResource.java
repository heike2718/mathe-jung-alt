// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import java.util.ArrayList;
import java.util.List;

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

import de.egladil.mja_admin_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_admin_api.domain.dto.SortDirection;
import de.egladil.mja_admin_api.domain.dto.Suchfilter;
import de.egladil.mja_admin_api.domain.generatoren.RaetselGeneratorService;
import de.egladil.mja_admin_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_admin_api.domain.raetsel.Outputformat;
import de.egladil.mja_admin_api.domain.raetsel.Raetsel;
import de.egladil.mja_admin_api.domain.raetsel.RaetselService;
import de.egladil.mja_admin_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_admin_api.domain.raetsel.dto.GeneratedImages;
import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_admin_api.infrastructure.validation.CsrfTokenValidator;
import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.Session;

/**
 * RaetselResource
 */
@Path("/raetsel/v1")
public class RaetselResource {

	@Context
	SecurityContext securityContext;

	@Inject
	RaetselService raetselService;

	@Inject
	RaetselGeneratorService generatorService;

	@Inject
	DeskriptorenService deskriptorenService;

	private final CsrfTokenValidator csrfTokenValidator = new CsrfTokenValidator();

	@GET
	@Path("size")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed("ADMIN")
	// @formatter:off
	public long zaehleRatsel(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$",
		message = "ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") final String suchstring,
		@QueryParam(value = "deskriptoren") @Pattern(
				regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
				message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren,
		@QueryParam(value = "typeDeskriptoren") @NotNull(message = "Angabe typeDeskriptoren ist erforderlich") final EnumType typeDeskriptoren) {
	// @formatter:on

		String deskriptorenOrdinal = checkAndTransformDeskriptoren(deskriptoren, typeDeskriptoren);

		if (StringUtils.isAllBlank(suchstring, deskriptorenOrdinal)) {

			return Long.valueOf(0);
		}

		return raetselService.zaehleRaetselMitSuchfilter(new Suchfilter(suchstring, deskriptorenOrdinal));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	// @formatter:off
	public List<RaetselsucheTreffer> sucheRaetsel(
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

		String deskriptorenOrdinal = checkAndTransformDeskriptoren(deskriptoren, typeDeskriptoren);

		if (StringUtils.isAllBlank(suchstring, deskriptorenOrdinal)) {

			return new ArrayList<>();
		}

		return raetselService.sucheRaetsel(new Suchfilter(suchstring, deskriptorenOrdinal), limit, offset,
			sortDirection);
	}

	@GET
	@Path("{raetselUuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response raetselDetailsLaden(@PathParam(value = "raetselUuid") final String raetselUuid) {

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

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, userPrincipal.getCsrfToken());
		Raetsel raetsel = raetselService.raetselAnlegen(payload, userPrincipal.getName());
		return Response.status(201).entity(raetsel).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response raetselAendern(final EditRaetselPayload payload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, userPrincipal.getCsrfToken());
		Raetsel raetsel = raetselService.raetselAendern(payload, userPrincipal.getName());
		return Response.status(200).entity(raetsel).build();

	}

	@GET
	@Path("{outputformat}/{raetselUuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public GeneratedImages raetselGenerieren(@PathParam(
		value = "outputformat") final Outputformat outputformat, @PathParam(
			value = "raetselUuid") final String raetselUuid, @QueryParam(
				value = "layoutAntwortvorschlaege") final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		GeneratedImages result = generatorService.produceOutputReaetsel(outputformat, raetselUuid, layoutAntwortvorschlaege);

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
