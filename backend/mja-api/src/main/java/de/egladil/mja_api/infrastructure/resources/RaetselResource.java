// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

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

import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.generatoren.RaetselGeneratorService;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedPDF;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.mja_api.infrastructure.validation.CsrfTokenValidator;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_auth.session.AuthenticatedUser;
import de.egladil.web.mja_auth.session.Session;

/**
 * RaetselResource
 */
@Path("/raetsel/v1")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Raetsel")
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
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "findRaetsel", summary = "Gibt alle Rätsel zurück, die auf die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			name = "suchstring",
			description = "Freitext zum suchen. Es erfolgt eine Volltextsuche über Schlüssel, Name, Kommentar, Frage und Lösung"),
		@Parameter(name = "deskriptoren", description = "kommaseparierte Liste von Deskriptoren-Identifizierern"),
		@Parameter(name = "typeDeskriptoren", description = "wie die Deskriptoren gesendet werden (NAME oder ID)"),
		@Parameter(
			name = "limit",
			description = "Pagination: pageSize"),
		@Parameter(
			name = "offset",
			description = "Pagination: pageIndex"),
		@Parameter(
			name = "sortDirection",
			description = "Sortierung. Es wird nach SCHLUESSEL sortiert.") })
	@APIResponse(
		name = "FindRaetselOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = RaetselsucheTreffer.class)))
	// @formatter:off
	public RaetselsucheTreffer findRaetsel(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{4,30}$",
		message = "ungültige Eingabe: mindestens 4 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") final String suchstring,
		@QueryParam(value = "deskriptoren") @Pattern(
			regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
			message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren,
		@QueryParam(value = "typeDeskriptoren") @NotNull(message = "Angabe typeDeskriptoren ist erforderlich") final EnumType typeDeskriptoren,
		@QueryParam(value = "limit") @DefaultValue("20") final int limit,
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
	@Path("{raetselID}")
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselDetailsLaden",
		summary = "Läd die Details des Rätsels mit der gegebenen ID")
	@Parameters({
		@Parameter(
			name = "raetselID",
			description = "technische ID des Rätsels") })
	@APIResponse(
		name = "FindRaetselByIDOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Raetsel.class)))
	@APIResponse(
		name = "RaetselByIDNotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response raetselDetailsLaden(@Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$",
		message = "raetselID enthält ungültige Zeichen") @PathParam(value = "raetselID") final String raetselUuid) {

		this.delayService.pause();

		Raetsel raetsel = raetselService.getRaetselZuId(raetselUuid);

		if (raetsel == null) {

			return Response.status(404).build();
		}

		return Response.status(200).entity(raetsel).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselAnlegen",
		summary = "neues Rätsel anlegen")
	@APIResponse(
		name = "CreateRaetselOKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Raetsel.class)))
	@APIResponse(
		name = "SchluesselConflict",
		description = "der gewählte schluessel ist schon vergeben",
		responseCode = "409", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "CreateRaetselServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response raetselAnlegen(final EditRaetselPayload payload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		this.delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		Raetsel raetsel = raetselService.raetselAnlegen(payload, userUuid);

		LOGGER.info("Raetsel angelegt: [raetsel={}, user={}]", raetsel.getId(), StringUtils.abbreviate(userUuid, 11));

		return Response.status(201).entity(raetsel).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselAendern",
		summary = "vorhandenes Rätsel ändern")
	@APIResponse(
		name = "UpdateRaetselOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Raetsel.class)))
	@APIResponse(
		name = "SchluesselConflict",
		description = "der geänderte schluessel ist schon vergeben",
		responseCode = "409", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "UpdateRaetselServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response raetselAendern(final EditRaetselPayload payload, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		this.delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";
		String csrfToken = authorizationEnabled ? userPrincipal.getCsrfToken() : "anonym";
		this.csrfTokenValidator.checkCsrfToken(csrfHeader, csrfToken, this.authorizationEnabled);

		Raetsel raetsel = raetselService.raetselAendern(payload, userUuid);

		LOGGER.info("Raetsel geaendert: [raetsel={}, user={}]", raetsel.getId(), StringUtils.abbreviate(userUuid, 11));

		return Response.status(200).entity(raetsel).build();
	}

	@GET
	@Path("PNG/{schluessel}")
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselImagesLaden",
		summary = "Läd die Vorschaubilder (png) des Rätsels")
	@Parameters({
		@Parameter(
			name = "schluessel",
			description = "Fachlicher Schlüssel des Rätsels") })
	@APIResponse(
		name = "LoadImagesRaetselOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Images.class)))
	@APIResponse(
		name = "LoadImagesRaetselServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Images raetselImagesLaden(@PathParam(value = "schluessel") final String schluessel) {

		return this.raetselService.findImagesZuSchluessel(schluessel);
	}

	@POST
	@Path("PNG/{raetselID}")
	@RolesAllowed("ADMIN")
	@Operation(
		operationId = "raetselImagesGenerieren",
		summary = "generiert die Vorschaubilder (png) des Rätsels")
	@Parameters({
		@Parameter(
			name = "raetselID",
			description = "technische ID des Rätsels"),
		@Parameter(
			name = "layoutAntwortvorschlaege",
			description = "Payload: Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)") })
	@APIResponse(
		name = "GenerateImagesRaetselOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Images.class)))
	@APIResponse(
		name = "GenerateImagesRaetselServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Images raetselImagesGenerieren(@PathParam(
		value = "raetselID") final String raetselUuid, @NotNull final LayoutAntwortvorschlaege layoutAntwortvorschlaege, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

		this.delayService.pause();

		AuthenticatedUser userPrincipal = (AuthenticatedUser) this.securityContext.getUserPrincipal();
		String userUuid = authorizationEnabled ? userPrincipal.getName() : "20721575-8c45-4201-a025-7a9fece1f2aa";

		Images result = generatorService.generatePNGsRaetsel(raetselUuid, layoutAntwortvorschlaege);

		LOGGER.info("Raetsel Images generiert: [raetsel={}, user={}]", raetselUuid, StringUtils.abbreviate(userUuid, 11));

		return result;
	}

	@GET
	@Path("PDF/{raetselID}")
	@RolesAllowed({ "ADMIN", "LEHRER", "PRIVAT", "STANDARD" })
	@Operation(
		operationId = "raetselPDFGenerieren",
		summary = "generiert ein PDF mit dem Rätsel")
	@Parameters({
		@Parameter(
			name = "raetselID",
			description = "technische ID des Rätsels"),
		@Parameter(
			name = "layoutAntwortvorschlaege",
			description = "Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)") })
	@APIResponse(
		name = "GeneratePDFRaetselOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = GeneratedPDF.class)))
	@APIResponse(
		name = "GeneratePDFRaetselServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public GeneratedPDF raetselPDFGenerieren(@Pattern(
		regexp = "^[a-fA-F\\d\\-]{1,36}$",
		message = "raetselID enthält ungültige Zeichen") @PathParam(
			value = "raetselID") final String raetselUuid, @QueryParam(
				value = "layoutAntwortvorschlaege") @NotNull final LayoutAntwortvorschlaege layoutAntwortvorschlaege, @HeaderParam(Session.CSRF_HEADER_NAME) final String csrfHeader) {

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
