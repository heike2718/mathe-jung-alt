// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import java.util.List;
import java.util.Optional;

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

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.medien.MedienService;
import de.egladil.mja_api.domain.medien.Medienart;
import de.egladil.mja_api.domain.medien.Mediensuchmodus;
import de.egladil.mja_api.domain.medien.dto.MediensucheResult;
import de.egladil.mja_api.domain.medien.dto.MediumDto;
import de.egladil.mja_api.domain.medien.dto.MediumQuelleDto;
import de.egladil.mja_api.domain.medien.dto.RaetselMediensucheTrefferItem;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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
import jakarta.ws.rs.core.Response.Status;

/**
 * MedienResource
 */
@Path("mja-api/medien")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Medien")
public class MedienResource {

	@Inject
	MedienService medienService;

	@GET
	@Path("{id}/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "getMedium", summary = "Gibt alle Medien zurück, die auf den suchmodus und die gegebene Suchanfrage passen.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "id",
			description = "technische ID eines Mediums"),
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MediensucheResult.class)))
	@APIResponse(
		name = "BadRequestResponse",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "NotFound",
		responseCode = "404",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response getMedium(@PathParam(value = "id") @Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "id enthält unerlaubte Zeichen") final String id) {

		Optional<MediumDto> opt = medienService.getMediumWithId(id);

		if (opt.isEmpty()) {

			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(opt.get()).build();
	}

	@GET
	@Path("/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findMedien",
		summary = "Gibt alle Medien zurück, die auf den suchmodus und die gegebene Suchanfrage passen. Admins sehen alle, Autoren nur die eigenen")
	@Parameters({
		@Parameter(
			in = ParameterIn.QUERY,
			name = "suchstring",
			description = "Freitext zum Suchen. Es erfolgt eine Suche mit like titel or like kommentar. Sortiert wird nach titel. Wenn nicht angegeben, werden alle aus der page geladen"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "limit",
			description = "Pagination: pageSize"),
		@Parameter(
			in = ParameterIn.QUERY,
			name = "offset",
			description = "Pagination: pageIndex"),
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MediensucheResult.class)))
	@APIResponse(
		name = "BadRequestResponse",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	// @formatter:off
	public Response findMedien(
		@QueryParam(value = "suchstring") @Pattern(
			regexp = MjaRegexps.VALID_SUCHSTRING_MEDIEN,
			message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen %+-_.,") final String suchstring,
		@QueryParam(value = "limit") @DefaultValue("20") final int limit,
		@QueryParam(value = "offset") @DefaultValue("0") final int offset) {
	// formatter:on

		Mediensuchmodus suchmodus = StringUtils.isBlank(suchstring) ? Mediensuchmodus.NOOP : Mediensuchmodus.SEARCHSTRING;

		MediensucheResult result = null;
		switch (suchmodus) {

		case NOOP -> result = medienService.loadMedien(limit, offset);
		case SEARCHSTRING -> result = medienService.findMedien(suchstring, limit, offset);
		default -> throw new IllegalArgumentException("Unexpected value: " + suchmodus);
		}

		return Response.ok(result).build();
	}

	@GET
	@Path("quelle/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "findMedienForUseInQuelle", summary = "Gibt eine Liste alle MedienDto der gegebenen Medienart zurück. Ist gedacht, um für Rätsel eine Quelle zu erzeugen.")
	@Parameters({
		@Parameter(
			in = ParameterIn.QUERY,
			name = "medienart",
			description = "Medienart. Sortiert wird nach titel.",
			required = true)
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = MediumQuelleDto.class)))
	@APIResponse(
		name = "BadRequestResponse",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response findMedienForUseInQuelle(@QueryParam(value="medienart")@NotNull(message = "medienart ist erforderlich") final Medienart medienart) {

		List<MediumQuelleDto> result = medienService.findMedienForUseInQuelle(medienart);
		return Response.ok(result).build();
	}

	@POST
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		operationId = "mediumAnlegen",
		summary = "neues Medium anlegen")
	@APIResponse(
		name = "OKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MediumDto.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "titelConflict",
		description = "der gewählte titel ist schon vergeben",
		responseCode = "409", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response mediumAnlegen(final MediumDto medium) {

		MediumDto result = medienService.mediumAnlegen(medium);

		return Response.ok(result).status(201).build();
	}

	@PUT
	@Path("v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
		operationId = "mediumAendern",
		summary = "Daten des Mediums ändern")
	@APIResponse(
		name = "OKResponse",
		responseCode = "201",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MediumDto.class)))
	@APIResponse(
		name = "BadRequest",
		responseCode = "400",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "Forbidden",
		description = "keine Änderungsberechtigung",
		responseCode = "403",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "NotFound",
		description = "Medium existiert nicht",
		responseCode = "404", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "titelConflict",
		description = "der neue titel ist schon vergeben",
		responseCode = "409", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response mediumAendern(final MediumDto medium) {

		MediumDto result = medienService.mediumAendern(medium);

		return Response.ok(result).status(200).build();
	}

	@GET
	@Path("{id}/raetsel/v1")
	@RolesAllowed({ "ADMIN", "AUTOR" })
	@Operation(
		operationId = "getRaetselMitMedium", summary = "Gibt alle Rätsel zurück, die das gegebene Medium als Quelle referenzieren.")
	@Parameters({
		@Parameter(
			in = ParameterIn.PATH,
			name = "id",
			description = "technische ID eines Mediums"),
	})
	@APIResponse(
		name = "OKResponse",
		responseCode = "200",
		content = @Content(
			mediaType = "application/json",
			schema = @Schema(type = SchemaType.ARRAY, implementation = RaetselMediensucheTrefferItem.class)))
	@APIResponse(
		name = "BadRequestResponse",
		responseCode = "400",
		description = "fehlgeschlagene Input-Validierung")
	@APIResponse(
		name = "NotAuthorized",
		responseCode = "401",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "NotFound",
		responseCode = "404",
		content = @Content(
			mediaType = "application/json"))
	@APIResponse(
		name = "ServerError",
		description = "server error",
		responseCode = "500", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = MessagePayload.class)))
	public Response getRaetselMitMedium(@PathParam(value = "id") @Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID, message = "id enthält unerlaubte Zeichen") final String id) {

		List<RaetselMediensucheTrefferItem> result = medienService.findRaetselWithMedium(id);
		return Response.ok(result).build();
	}
}
