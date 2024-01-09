// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.io.File;
import java.util.Optional;

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

import de.egladil.mja_api.domain.aufgabensammlungen.AufgabensammlungenService;
import de.egladil.mja_api.domain.aufgabensammlungen.AufgabensammlungenSortattribute;
import de.egladil.mja_api.domain.aufgabensammlungen.AufgabensammlungenSuchparameter;
import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungDetails;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungSucheTreffer;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungSucheTrefferItem;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungPayload;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.EditAufgabensammlungselementPayload;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedFile;
import de.egladil.mja_api.domain.utils.DevDelayService;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * AufgabensammlungenResource
 */
@Path("mja-api/aufgabensammlungen")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Tag(name = "Aufgabensammlungen")
public class AufgabensammlungenResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AufgabensammlungenResource.class);

	@Inject
	AuthenticationContext authCtx;

	@Inject
	DevDelayService delayService;

	@Inject
	AufgabensammlungenService aufgabensammlungenService;

	@GET
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR", "STANDARD" })
	@Operation(
		operationId = "findAufgabensammlungen",
		summary = "Gibt alle Aufgabensammlungen zurück, die auf die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			in = ParameterIn.QUERY,
			name = "name",
			description = "Teil des Namens der Gruppe (Suche mit like)"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "schwierigkeitsgrad",
			description = "Klassenstufe, für die die Aufgabensammlung gedacht ist (enum)"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "referenztyp",
			description = "Kontext zur Interpretation des Parameters 'referenz'"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "referenz",
			description = "ID im alten Aufgabenarchiv"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "sortAttribute",
			description = "Attribut, nach dem sortiert wird"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "sortDirection",
			description = "Sortierrichtung für das gewählte Attribut"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "limit",
			description = "Pagination: pageSize"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "offset",
			description = "Pagination: pageIndex") })
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = AufgabensammlungSucheTreffer.class)))
	// @formatter:off
	public AufgabensammlungSucheTreffer findAufgabensammlungen(
		@QueryParam(value = "name") @Pattern(regexp = "[\\w äöüß\\:\\-\\.\\,]*", message = "name enthält unerlaubte Zeichen")
		@Size(min = 1, max = 100, message = "nicht mehr als 100 Zeichen") final String name,
		@QueryParam(value = "schwierigkeitsgrad") final Schwierigkeitsgrad schwierigkeitsgrad,
		@QueryParam(value = "referenztyp") final Referenztyp referenztyp,
		@QueryParam(value = "referenz") @Pattern(regexp = "^[\\w äöüß]{1,20}$" , message = "referenz enthält unerlaubte Zeichen")
		@Size(min = 1, max = 36, message = "nicht mehr als 36 Zeichen") final String referenz,
		@QueryParam(value = "sortAttribute") @DefaultValue("name") final AufgabensammlungenSortattribute sortAttribute,
		@QueryParam(value = "sortDirection") @DefaultValue("asc")  final SortDirection sortDirection,
		@QueryParam(value = "limit") @DefaultValue("20") final int limit,
		@QueryParam(value = "offset") @DefaultValue("0") final int offset) {
		// @formatter:off

		if (limit > 50) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("nicht mehr als 50 auf einmal abfragen.")).build());
		}

		AuthenticatedUser user = authCtx.getUser();

		AufgabensammlungenSuchparameter suchparameter = new AufgabensammlungenSuchparameter(name,
			schwierigkeitsgrad,
			referenztyp,
			referenz,
			sortAttribute,
			sortDirection,
			user.getName(),
			user.getBenutzerart()
		);

		return aufgabensammlungenService.findAufgabensammlungen(suchparameter, limit, offset);
	}

	@POST
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		operationId = "aufgabensammlungAnlegen",
		summary = "neue Aufgabensammlung anlegen")
	@APIResponse(
		name = "OKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AufgabensammlungSucheTrefferItem.class)))
	@APIResponse(
		name = "BadRequest",
		description = "Input-Validierung schlug fehl",
		responseCode = "400", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Unauthorized",
		description = "nur Admins und Autoren dürfen Aufgabensammlungen anlegen (vorerst)",
		responseCode = "401")
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response aufgabensammlungAnlegen(@Valid final EditAufgabensammlungPayload requestPayload) {

		delayService.pause();

		AufgabensammlungSucheTrefferItem raetselsammlung = aufgabensammlungenService.aufgabensammlungAnlegen(requestPayload);
		return Response.status(201).entity(raetselsammlung).build();
	}

	@PUT
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		operationId = "aufgabensammlungAendern",
		summary = "neue Aufgabensammlung anlegen")
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AufgabensammlungSucheTrefferItem.class)))
	@APIResponse(
		name = "BadRequest",
		description = "Input-Validierung schlug fehl",
		responseCode = "400", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Unauthorized",
		description = "nur Admins und Autoren dürfen Aufgabensammlungen ändern (vorerst)",
		responseCode = "401")
	@APIResponse(
		name = "Forbidden",
		description = "Admins dürfen jede Aufgabensammlung ändern, Autoren nur eigene.",
		responseCode = "403", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response aufgabensammlungAendern(final EditAufgabensammlungPayload requestPayload) {

		delayService.pause();

		AufgabensammlungSucheTrefferItem raetselsammlung = aufgabensammlungenService.aufgabensammlungBasisdatenAendern(requestPayload);
		return Response.status(200).entity(raetselsammlung).build();
	}

	@GET
	@Path("{aufgabensammlungID}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR", "STANDARD" })
	@Operation(
		operationId = "aufgabensammlungDetailsLaden",
		summary = "Läd die Details der Aufgabensammlung mit der gegebenen ID")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "aufgabensammlungID",
			description = "technische ID der Aufgabensammlung") })
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AufgabensammlungDetails.class)))
	@APIResponse(
		name = "BadRequest",
		description = "Input-Validierung schlug fehl",
		responseCode = "400", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Unauthorized",
		responseCode = "401")
	@APIResponse(
		name = "Forbidden",
		description = "Admins dürfen alle Details sehen, Autoren nur die public oder eigenen, Standarduser nur die freigegebenen public oder eigene",
		responseCode = "403")
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public AufgabensammlungDetails aufgabensammlungDetailsLaden(@PathParam(value = "aufgabensammlungID") @Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "aufgabensammlungID enthält ungültige Zeichen") final String aufgabensammlungID) {

		Optional<AufgabensammlungDetails> optDetails = aufgabensammlungenService.loadDetails(aufgabensammlungID);

		if (optDetails.isEmpty()) {
			throw new WebApplicationException(Response.status(404).entity(MessagePayload.error("kein Treffer")).build());
		}

		AufgabensammlungDetails result = optDetails.get();
		return result;
	}

	@POST
	@Path("{aufgabensammlungID}/elemente/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "elementAnlegen",
		summary = "Legt ein neues Element in einer Aufgabensammlung an")
	@Parameters({
		@Parameter(in = ParameterIn.PATH, name = "aufgabensammlungID", description = "ID der Aufgabensammlung.")
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AufgabensammlungDetails.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	@APIResponse(
		name = "Unauthorized",
		description = "nur Admins und Autoren dürfen Aufgabensammlungen ändern (vorerst)",
		responseCode = "401")
	@APIResponse(
		name = "Forbidden",
		description = "Admins dürfen jede Aufgabensammlung ändern, Autoren nur eigene.",
		responseCode = "403", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "NotFound",
		description = "Die Aufgabensammlung oder das Rätsel mit dem fachlichen SCHLUESSEL gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "Conflict",
		description = "Nummer würde doppelt vergeben",
		responseCode = "409", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "Serverfehler",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public AufgabensammlungDetails elementAnlegen(@PathParam(value = "aufgabensammlungID") @Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "aufgabensammlungID enthält ungültige Zeichen") final String aufgabensammlungID, final EditAufgabensammlungselementPayload element) {

		return this.aufgabensammlungenService.elementAnlegen(aufgabensammlungID, element);
	}


	@PUT
	@Path("{aufgabensammlungID}/elemente/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "elementAendern",
		summary = "Ändert das Element einer Aufgabensammlung. Es können nur Nummer und Punkte geändert werden. Wenn der Schlüssel nicht stimmt, muss es gelöscht und neu angelegt werden.")
	@Parameters({
		@Parameter(in = ParameterIn.PATH, name = "aufgabensammlungID", description = "ID der Aufgabensammlung.")
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AufgabensammlungDetails.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	@APIResponse(
		name = "Unauthorized",
		description = "nur Admins und Autoren dürfen Aufgabensammlungen ändern (vorerst)",
		responseCode = "401")
	@APIResponse(
		name = "Forbidden",
		description = "Admins dürfen jede Aufgabensammlung ändern, Autoren nur eigene.",
		responseCode = "403", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "NotFound",
		description = "Die Aufgabensammlung oder das Element gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "Gibt es nicht",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public AufgabensammlungDetails elementAendern(@PathParam(value = "aufgabensammlungID") @Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "aufgabensammlungID enthält ungültige Zeichen") final String aufgabensammlungID, final EditAufgabensammlungselementPayload element) {

		return this.aufgabensammlungenService.elementAendern(aufgabensammlungID, element);
	}

	@DELETE
	@Path("{aufgabensammlungID}/elemente/{elementID}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "elementLoeschen",
		summary = "Löscht das Element einer Aufgabensammlung")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "aufgabensammlungID",
			description = "ID der Aufgabensammlung") })
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = AufgabensammlungDetails.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	@APIResponse(
		name = "Unauthorized",
		description = "nur Admins und Autoren dürfen Aufgabensammlungen ändern (vorerst)",
		responseCode = "401")
	@APIResponse(
		name = "Forbidden",
		description = "Admins dürfen jede Aufgabensammlung ändern, Autoren nur eigene.",
		responseCode = "403", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "NotFound",
		description = "Die Aufgabensammlung oder das Element gibt es nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "Gibt es nicht",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public AufgabensammlungDetails elementLoeschen(@PathParam(value = "aufgabensammlungID") @Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "aufgabensammlungID enthält ungültige Zeichen") final String aufgabensammlungID, @PathParam(value = "elementID") @Pattern(
			regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
			message = "aufgabensammlungID enthält ungültige Zeichen") final String elementID) {

		return aufgabensammlungenService.elementLoeschen(aufgabensammlungID, elementID);
	}

	@GET
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Path("{aufgabensammlungID}/vorschau/v1")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "printVorschau",
		summary = "Generiert aus der Aufgabensammlung mit der gegebenen ID ein PDF. Diese API funktioniert für Aufgabensammlungen mit beliebigem Status. Aufgaben und Lösungen werden zusammen gedruckt. . Es wird immer mit ANKREUTZABELLE gedruckt")
	@Parameters({
		@Parameter(in = ParameterIn.PATH, name = "aufgabensammlungID", description = "ID der Aufgabensammlung, für das ein Quiz gedruckt wird."),
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
			required = false)
	})
	@APIResponse(
		name = "OKResponse",
		description = "PDF erfolgreich generiert",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = GeneratedFile.class)))
	@APIResponse(
		name = "Unauthorized",
		description = "nur Admins und Autoren dürfen eine Vorschau generieren",
		responseCode = "401")
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "ServerError",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public GeneratedFile printVorschau(
		@PathParam(value = "aufgabensammlungID") @Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "Pfad (ID) enthält ungültige Zeichen") final String aufgabensammlungID,
		@QueryParam(value = "layoutAntwortvorschlaege") @NotNull final LayoutAntwortvorschlaege layoutAntwortvorschlaege,
		@QueryParam(value = "font") final FontName font,
		@QueryParam(value = "size") final Schriftgroesse schriftgroesse) {
	// @formatter:on

		FontName theFont = font == null ? FontName.STANDARD : font;
		Schriftgroesse theSchriftgroesse = schriftgroesse == null ? Schriftgroesse.NORMAL : schriftgroesse;

		LOGGER.debug("font={}, theFont={}, size={}, theSize={}", font, theFont, schriftgroesse, theSchriftgroesse);

		return aufgabensammlungenService.printVorschau(aufgabensammlungID, theFont, theSchriftgroesse, layoutAntwortvorschlaege);
	}

	@GET
	@Path("{aufgabensammlungID}/latex/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(
		operationId = "downloadLaTeXSource",
		summary = "Generiert aus der Aufgabensammlung mit der gegebenen ID mehrere LaTeX-Dateien. Eine ist expandiert und enthält erst die Aufgaben, dann die Lösungen, zwei weitere importieren einzelne LaTeX-Dateien. Alle erforderlichen sourcen werden heruntergeladen, so dass nach dem Verschieben der eingebundenen Grafiken sofort generiert werden kann. Es wird ein Zip-Archiv generiert.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH, name = "aufgabensammlungID",
			description = "ID der Aufgabensammlung, für das ein Quiz gedruckt wird.",
			required = true),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "layoutAntwortvorschlaege",
			description = "Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "font",
			description = "Font, mit dem der Text gedruckt werden soll. Wenn null, dann wird der Standard-LaTeX-Font (STANDARD) verwendet.",
			required = false),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "size",
			description = "wird in LaTeX-Größenangaben umgewandelt.",
			required = false)
	})
	@APIResponse(
		name = "OKResponse",
		description = "LaTeX erfolgreich generiert",
		responseCode = "200",
		content = @Content(
			mediaType = "application/octet-stram"))
	@APIResponse(
		name = "Unauthorized",
		description = "nur Admins und Autoren dürfen Aufgabensammlungen LaTeX herunterladen",
		responseCode = "401")
	@APIResponse(
		name = "Forbidden",
		description = "Admins dürfen das LaTeX jeder Aufgabensammlung herunterladen, Autoren nur das der eigenen.",
		responseCode = "403", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "Servererror",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public Response downloadLaTeX(
		@PathParam( value = "aufgabensammlungID") @Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "Pfad (ID) enthält ungültige Zeichen") final String aufgabensammlungID,
		@QueryParam(value = "layoutAntwortvorschlaege") @NotNull final LayoutAntwortvorschlaege layoutAntwortvorschlaege,
		@QueryParam(value = "font") final FontName font,
		@QueryParam(value = "size") final Schriftgroesse schriftgroesse) {
   // @formatter:on

		FontName theFont = font == null ? FontName.STANDARD : font;
		Schriftgroesse theSchriftgroesse = schriftgroesse == null ? Schriftgroesse.NORMAL : schriftgroesse;

		LOGGER.debug("font={}, theFont={}, size={}, theSize={}", font, theFont, schriftgroesse, theSchriftgroesse);

		File generatedFile = aufgabensammlungenService.downloadLaTeXSources(aufgabensammlungID, theFont, theSchriftgroesse,
			layoutAntwortvorschlaege);

		LOGGER.debug("zip generiert");

		ResponseBuilder ok = Response.ok().header("Content-Disposition", "attachment;filename=" + generatedFile.getName());

		return ok.entity((Object) generatedFile).build();
	}

	@GET
	@Path("{aufgabensammlungID}/arbeitsblatt/v1")
	@RolesAllowed({ "ADMIN", "AUTOR", "STANDARD" })
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "printArbeitsblatt",
		summary = "Generiert aus der Aufgabensammlung mit der gegebenen ID ein PDF. Die Lösungen werden am Ende des PDFs von den Aufgaben separiert gedruckt. Die Sortierung erfolgt anhand der Nummer der Elemente. Die aufrufende Person muss für diese Aufgabensammlung berechtigt sein. Es wird immer ohne Antwortvorschläge gedruckt.")
	@Parameters({
		@Parameter(
			name = "aufgabensammlungID", description = "ID der Aufgabensammlung, für das ein Quiz gedruckt wird.", required = true),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "layoutAntwortvorschlaege",
			description = "Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)",
			required = false),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "font",
			description = "Font, mit dem der Text gedruckt werden soll. Wenn null, dann wird der Standard-LaTeX-Font (STANDARD) verwendet.",
			required = false),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "size",
			description = "wird in LaTeX-Größenangaben umgewandelt.",
			required = false)
	})
	@APIResponse(
		name = "OKResponse",
		description = "Quiz erfolgreich geladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = GeneratedFile.class)))
	@APIResponse(
		name = "Unauthorized",
		description = "nur authentifizierte Benutzer mit den erforderlichen Rollen dürfen Arbeitsblätter generieren",
		responseCode = "401")
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "ServerError",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public GeneratedFile printArbeitsblattMitLoesungen(
		@PathParam(value = "aufgabensammlungID") @Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "Pfad (ID) enthält ungültige Zeichen") final String aufgabensammlungID,
		@QueryParam(value = "font") final FontName font,
		@QueryParam(value = "size") final Schriftgroesse schriftgroesse,
		@QueryParam(value = "layoutAntwortvorschlaege") final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {
	// @formatter:on

		FontName theFont = font == null ? FontName.STANDARD : font;
		Schriftgroesse theSchriftgroesse = schriftgroesse == null ? Schriftgroesse.NORMAL : schriftgroesse;
		LayoutAntwortvorschlaege theLayout = layoutAntwortvorschlaege == null ? LayoutAntwortvorschlaege.NOOP
			: layoutAntwortvorschlaege;

		LOGGER.debug("font={}, theFont={}, size={}, theSize={}", font, theFont, schriftgroesse, theSchriftgroesse);

		return aufgabensammlungenService.printArbeitsblattMitLoesungen(aufgabensammlungID, theFont, theSchriftgroesse, theLayout);
	}

	@GET
	@Path("{aufgabensammlungID}/knobelkartei/v1")
	@RolesAllowed({ "ADMIN", "AUTOR", "STANDARD" })
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Operation(
		operationId = "printKnobelkartei",
		summary = "Generiert aus der Aufgabensammlung mit der gegebenen ID ein PDF, in dem jede Seite genau ein Rätsel enthält. Frage und Lösung werden nacheinander auf einzelne Blätter gedruckt. Die Sortierung erfolgt anhand der Nummer der Elemente. Die aufrufende Person muss für diese Aufgabensammlung berechtigt sein. Es wird immer ohne Antwortvorschläge gedruckt.")
	@Parameters({
		@Parameter(
			name = "aufgabensammlungID", description = "ID der Aufgabensammlung, für das ein Quiz gedruckt wird.", required = true),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "layoutAntwortvorschlaege",
			description = "Layout, wie die Antwortvorschläge dargestellt werden sollen, wenn es welche gibt (Details siehe LayoutAntwortvorschlaege)",
			required = false),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "font",
			description = "Font, mit dem der Text gedruckt werden soll. Wenn null, dann wird der Standard-LaTeX-Font (STANDARD) verwendet.",
			required = false),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "size",
			description = "wird in LaTeX-Größenangaben umgewandelt.",
			required = false)
	})
	@APIResponse(
		name = "OKResponse",
		description = "Quiz erfolgreich geladen",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = GeneratedFile.class)))
	@APIResponse(
		name = "Unauthorized",
		description = "nur authentifizierte Benutzer mit den erforderlichen Rollen dürfen Knobelkarteien generieren",
		responseCode = "401")
	@APIResponse(
		name = "NotFound",
		description = "Gibt es nicht",
		responseCode = "404")
	@APIResponse(
		name = "ServerError",
		description = "Serverfehler",
		responseCode = "500",
		content = @Content(schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public GeneratedFile printKnobelkartei(
		@PathParam(value = "aufgabensammlungID") @Pattern(regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "Pfad (ID) enthält ungültige Zeichen") final String aufgabensammlungID,
		@QueryParam(value = "font") final FontName font,
		@QueryParam(value = "size") final Schriftgroesse schriftgroesse,
		@QueryParam(value = "layoutAntwortvorschlaege") final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {
	// @formatter:on

		FontName theFont = font == null ? FontName.STANDARD : font;
		Schriftgroesse theSchriftgroesse = schriftgroesse == null ? Schriftgroesse.NORMAL : schriftgroesse;
		LayoutAntwortvorschlaege theLayout = layoutAntwortvorschlaege == null ? LayoutAntwortvorschlaege.NOOP
			: layoutAntwortvorschlaege;

		LOGGER.debug("font={}, theFont={}, size={}, theSize={}, theLayout={}", font, theFont, schriftgroesse, theSchriftgroesse,
			theLayout);

		return aufgabensammlungenService.printKartei(aufgabensammlungID, theFont, theSchriftgroesse, theLayout);
	}

}
