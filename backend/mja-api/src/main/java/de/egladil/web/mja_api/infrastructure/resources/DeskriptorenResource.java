// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.mja_api.infrastructure.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.egladil.web.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.web.mja_shared.domain.deskriptoren.DeskriptorSuchkontext;

/**
 * DeskriptorenResource
 */
@Path("/deskriptoren/v1")
public class DeskriptorenResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "ADMIN", "PRIVAT", "LEHRER", "STANDARD" })
	public List<Deskriptor> loadDeskriptoren() {

		List<Deskriptor> trefferliste = Deskriptor.listAll();

		return trefferliste.stream()
			.filter(d -> !d.admin && DeskriptorSuchkontext.RAETSEL.toString().equals(d.kontext)).toList();
	}

}
