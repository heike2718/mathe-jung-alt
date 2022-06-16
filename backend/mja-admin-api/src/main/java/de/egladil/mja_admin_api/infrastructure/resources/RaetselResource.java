// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EnumType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

/**
 * RaetselResource
 */
@Path("/raetsel/v1")
public class RaetselResource {

	@Inject
	RaetselService raetselService;

	@Inject
	RaetselGeneratorService generatorService;

	@Inject
	DeskriptorenService deskriptorenService;

	@GET
	@Path("size")
	@Produces(MediaType.TEXT_PLAIN)
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
	public Response raetselAnlegen(final EditRaetselPayload payload) {

		// TODO: aus der Session holen!!!
		String uuidAendernderUser = "20721575-8c45-4201-a025-7a9fece1f2aa";

		Raetsel raetsel = raetselService.raetselAnlegen(payload, uuidAendernderUser);
		return Response.status(201).entity(raetsel).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response raetselAendern(final EditRaetselPayload payload) {

		// TODO: aus der Session holen!!!
		String uuidAendernderUser = "20721575-8c45-4201-a025-7a9fece1f2aa";

		Raetsel raetsel = raetselService.raetselAendern(payload, uuidAendernderUser);
		return Response.status(200).entity(raetsel).build();

	}

	@GET
	@Path("{outputformat}/{raetselUuid}")
	@Produces(MediaType.APPLICATION_JSON)
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
