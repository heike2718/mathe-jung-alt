// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.egladil.mathe_jung_alt_ws.domain.generatoren.RaetselGeneratorService;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.AnzeigeAntwortvorschlaegeTyp;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Outputformat;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.RaetselService;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.EditRaetselPayload;

/**
 * AdminRaetselResource
 */
@Path("/admin/raetsel")
public class AdminRaetselResource {

	@Inject
	RaetselService raetselService;

	@Inject
	RaetselGeneratorService generatorService;

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
		return Response.status(201).entity(raetsel).build();

	}

	@GET
	@Path("{outputformat}/{raetselUuid}")
	public Response raetselGenerieren(@PathParam(
		value = "outputformat") final Outputformat outputformat, @PathParam(
			value = "raetselUuid") final String raetselUuid, @QueryParam(
				value = "antworttyp") final AnzeigeAntwortvorschlaegeTyp anzeigeAntwortvorschlaege) {

		String url = generatorService.produceOutputReaetsel(outputformat, raetselUuid, anzeigeAntwortvorschlaege);

		return Response.ok(url).build();
	}

}
