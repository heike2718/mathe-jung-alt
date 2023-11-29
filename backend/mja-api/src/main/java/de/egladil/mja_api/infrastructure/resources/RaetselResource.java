// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.SuchmodusDeskriptoren;
import de.egladil.mja_api.domain.SuchmodusVolltext;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.SessionService;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.dto.AnzahlabfrageResponseDto;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.generatoren.impl.RaetselGeneratorService;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * RaetselResource
 */
@Path("mja-api/raetsel")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Raetsel")
public class RaetselResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselResource.class);

	@Inject
	SessionService sessionService;

	@Inject
	DevDelayService delayService;

	@Inject
	RaetselService raetselService;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	RaetselGeneratorService generatorService;

	@Inject
	DeskriptorenService deskriptorenService;

	@GET
	@Path("admin/v2")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findRaetselAdmin", summary = "Gibt alle Rätsel zurück, die auf die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			in = ParameterIn.QUERY,
			name = "suchstring",
			description = "Freitext zum Suchen. Es erfolgt eine Volltextsuche über Schlüssel, Name, Kommentar, Frage und Lösung. Mehrere Worte werden je nach Modus mit AND oder mit OR verknüft."),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "deskriptoren",
			description = "kommaseparierte Liste von Deskriptoren-Identifizierern. Bei typeDeskriptoren=ORDINAL die technischen IDs sonst der Name."),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "modeFullTextSearch",
			description = "sollen mehrere Worte mit AND (INTERSECTION) oder mit OR (UNION) gesucht werden?"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "search_mode_for_descriptors", description = "SQL-Operator mit dem nach Deskriptoren gesucht wird."),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "typeDeskriptoren", description = "wie die Deskriptoren gesendet: die technischen IDs oder die Namen.",
			required = true),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "limit",
			description = "Pagination: pageSize"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "offset",
			description = "Pagination: pageIndex"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "sortDirection",
			description = "Sortierung. Es wird nach SCHLUESSEL sortiert.") })
	@APIResponse(
		name = "FindRaetselAdminOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = RaetselsucheTreffer.class)))
	// @formatter:off
	public RaetselsucheTreffer findRaetselAdmin(
		@QueryParam(value = "suchstring") @Pattern(
		regexp = MjaRegexps.VALID_SUCHSTRING,
		message = "ungültige Eingabe: mindestens 4 höchstens 200 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen %+-_.,") final String suchstring,
		@QueryParam(value = "deskriptoren") @Pattern(
			regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
			message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren,
		@QueryParam(value = "modeFullTextSearch") @DefaultValue("UNION") final SuchmodusVolltext modus,
		@QueryParam(value = "searchModeForDescriptors") @DefaultValue("LIKE") final SuchmodusDeskriptoren modusDeskriptoren,
		@QueryParam(value = "typeDeskriptoren") @NotNull(message = "Angabe typeDeskriptoren ist erforderlich") final EnumType typeDeskriptoren,
		@QueryParam(value = "limit") @DefaultValue("20") final int limit,
		@QueryParam(value = "offset") @DefaultValue("0") final int offset,
		@QueryParam(value = "sortDirection")  @DefaultValue("asc") final SortDirection sortDirection) {
		// @formatter:on

		this.delayService.pause();

		String deskriptorenOrdinal = checkAndTransformDeskriptoren(deskriptoren, typeDeskriptoren);

		if (StringUtils.isAllBlank(suchstring, deskriptorenOrdinal)) {

			return new RaetselsucheTreffer();
		}

		String theSuchstring = suchstring;

		if (StringUtils.isNotBlank(suchstring) && suchstring.contains("%20")) {

			theSuchstring = suchstring.replaceAll("%20", " ");
		}

		Suchfilter suchfilter = new Suchfilter(theSuchstring, deskriptorenOrdinal);
		suchfilter.setModusVolltext(modus);
		suchfilter.setModusDeskriptoren(modusDeskriptoren);

		return raetselService.sucheRaetsel(suchfilter, limit, offset,
			sortDirection);
	}

	@GET
	@Path("v2")
	@RolesAllowed({ "ADMIN", "AUTOR", "STANDARD", "LEHRER" })
	@Operation(
		operationId = "findRaetselPublic", summary = "Gibt alle Rätsel mit den gegebenen Deskriptoren zurück")
	@Parameters({
		@Parameter(
			in = ParameterIn.QUERY,
			name = "deskriptoren",
			description = "kommaseparierte Liste von Deskriptoren-Identifizierern. Bei typeDeskriptoren=ORDINAL die technischen IDs sonst der Name."),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "searchModeForDescriptors", description = "SQL-Operator mit dem nach Deskriptoren gesucht wird."),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "typeDeskriptoren", description = "wie die Deskriptoren gesendet: die technischen IDs oder die Namen.",
			required = true),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "limit",
			description = "Pagination: pageSize"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "offset",
			description = "Pagination: pageIndex"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "sortDirection",
			description = "Sortierung. Es wird nach SCHLUESSEL sortiert.") })
	@APIResponse(
		name = "FindRaetselPublicOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = RaetselsucheTreffer.class)))
	@APIResponse(
		name = "FindRaetselPublicBadRequestResponse",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	// @formatter:off
	public RaetselsucheTreffer findRaetselPublic(
		@QueryParam(value = "deskriptoren") @Pattern(
			regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
			message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren,
		@QueryParam(value = "searchModeForDescriptors") @DefaultValue("LIKE") final SuchmodusDeskriptoren modusDeskriptoren,
		@QueryParam(value = "typeDeskriptoren") @NotNull(message = "Angabe typeDeskriptoren ist erforderlich") final EnumType typeDeskriptoren,
		@QueryParam(value = "limit") @DefaultValue("20") final int limit,
		@QueryParam(value = "offset") @DefaultValue("0") final int offset,
		@QueryParam(value = "sortDirection")  @DefaultValue("asc") final SortDirection sortDirection) {
	// @formatter:on

		this.delayService.pause();

		String deskriptorenOrdinal = checkAndTransformDeskriptoren(deskriptoren, typeDeskriptoren);

		if (StringUtils.isBlank(deskriptorenOrdinal)) {

			return new RaetselsucheTreffer();
		}

		Suchfilter suchfilter = new Suchfilter(null, deskriptorenOrdinal);
		suchfilter.setModusDeskriptoren(modusDeskriptoren);

		return raetselService.sucheRaetsel(suchfilter, limit, offset,
			sortDirection);
	}

	@GET
	@Path("/public/anzahl/v1")
	@PermitAll
	@Operation(
		operationId = "getAnzahlFreigegebenerRaetsel",
		summary = "Gibt die Anzahl der zum Abfragezeitpunkt freigegebenen Rätsel zurück.")
	@APIResponse(
		name = "GetAnzahlFreigegebenerRaetselOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AnzahlabfrageResponseDto.class)))
	@APIResponse(
		name = "GetAnzahlFreigegebenerRaetselServerError",
		responseCode = "500",
		description = "Fehler aufgetreten",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public Response getAnzahlFreigegebenerRaetsel() {
	// @formatter:on

		return Response.ok(raetselService.zaehleFreigegebeneRaetsel()).build();
	}

	@GET
	@Path("{raetselID}/v1")
	@Authenticated
	@Operation(
		operationId = "raetselDetailsLaden",
		summary = "Läd die Details des Rätsels mit der gegebenen ID")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
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
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Consumes(MediaType.APPLICATION_JSON)
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
	public Response raetselAnlegen(final EditRaetselPayload payload) {

		this.delayService.pause();

		Raetsel raetsel = raetselService.raetselAnlegen(payload);
		return Response.status(201).entity(raetsel).build();

	}

	@PUT
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Consumes(MediaType.APPLICATION_JSON)
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
	public Response raetselAendern(final EditRaetselPayload payload) {

		this.delayService.pause();

		Raetsel raetsel = raetselService.raetselAendern(payload);
		return Response.status(200).entity(raetsel).build();
	}

	@GET
	@Path("PNG/{schluessel}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "raetselImagesLaden",
		summary = "Läd die Vorschaubilder (png) des Rätsels")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
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

		LOGGER.info("SCHLUESSEL=" + schluessel);

		return this.raetselService.findImagesZuSchluessel(schluessel);
	}

	@POST
	@Path("PNG/{raetselID}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "raetselImagesGenerieren",
		summary = "generiert die Vorschaubilder (png) des Rätsels")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "raetselID",
			description = "technische ID des Rätsels"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "layoutAntwortvorschlaege",
			description = "Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)",
			required = true),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "font",
			description = "Font, mit dem der Text gedruckt werden soll. Wenn null, dann wird der Standard-LaTeX-Font (STANDARD) verwendet.",
			required = false),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "size",
			description = "wird in LaTeX-Größenangaben umgewandelt.",
			required = false) })
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
	// @formatter:off
	public Images raetselImagesGenerieren(
		@Pattern(regexp = "^[a-fA-F\\d\\-]{1,36}$", message = "raetselID enthält ungültige Zeichen") @PathParam(value = "raetselID") final String raetselUuid,
		@QueryParam(value = "layoutAntwortvorschlaege") @NotNull final LayoutAntwortvorschlaege layoutAntwortvorschlaege,
		@QueryParam(value = "font") @DefaultValue("STANDARD") final FontName font,
		@QueryParam(value = "size") @DefaultValue("NORMAL") final Schriftgroesse schriftgroesse) {
	// @formatter:on

		this.delayService.pause();

		LOGGER.debug("font={}, schriftgroesse={}", font, schriftgroesse);

		Images result = generatorService.generatePNGsRaetsel(raetselUuid, layoutAntwortvorschlaege, font, schriftgroesse);

		// jetzt liegt schluessel.png und evtl. schluessel_l.png im latex.base.dir und muss verschoben werden.

		return result;
	}

	@GET
	@Path("PDF/{raetselID}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR", "STANDARD", "LEHRER" })
	@Operation(
		operationId = "raetselPDFGenerieren",
		summary = "generiert ein PDF mit dem Rätsel")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "raetselID",
			description = "technische ID des Rätsels"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "layoutAntwortvorschlaege",
			description = "Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)",
			required = true),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "font",
			description = "Font, mit dem der Text gedruckt werden soll. Wenn null, dann wird der Standard-LaTeX-Font (STANDARD) verwendet.",
			required = false),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "size",
			description = "wird in LaTeX-Größenangaben umgewandelt.",
			required = false) })
	@APIResponse(
		name = "GeneratePDFRaetselOKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = GeneratedFile.class)))
	@APIResponse(
		name = "GeneratePDFRaetselServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public GeneratedFile raetselPDFGenerieren(
		@Pattern(regexp = "^[a-fA-F\\d\\-]{1,36}$", message = "raetselID enthält ungültige Zeichen") @PathParam(value = "raetselID") final String raetselUuid,
		@QueryParam(value = "layoutAntwortvorschlaege") @NotNull final LayoutAntwortvorschlaege layoutAntwortvorschlaege,
		@QueryParam(value = "font") @DefaultValue("STANDARD") final FontName font,
		@QueryParam(value = "size") @DefaultValue("NORMAL") final Schriftgroesse schriftgroesse) {
	// @formatter:on

		this.delayService.pause();

		LOGGER.debug("font={}, schriftgroesse={}", font, schriftgroesse);

		GeneratedFile result = generatorService.generatePDFRaetsel(raetselUuid, layoutAntwortvorschlaege, font,
			schriftgroesse);
		return result;
	}

	@GET
	@Path("latexlogs/{schluessel}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "downloadLatexLogFiles",
		summary = "Läd aus dem LaTeX-Verzeichnis die Dateien schluessel.log und schluessel_l.log herunter, wenn sie existieren.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "schluessel",
			description = "5stelliger SCHLUESSEL eines Rätsels") })
	@APIResponse(
		name = "DownloadLatexLogFilesOK-Response",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = GeneratedFile[].class)))
	public GeneratedFile[] downloadLatexLogFiles(@Pattern(
		regexp = "^[\\d]{5}$",
		message = "schluessel muss aus genau 5 Ziffern bestehen") @PathParam(value = "schluessel") final String schluessel) {

		return raetselFileService.getLaTeXLogs(schluessel);
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
