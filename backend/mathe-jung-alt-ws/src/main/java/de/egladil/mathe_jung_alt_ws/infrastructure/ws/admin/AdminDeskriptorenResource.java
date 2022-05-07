// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorSuchkontext;
import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorenService;
import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.impl.DeskriptorenRepository;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.panache.common.Sort;

/**
 * AdminDeskriptorenResource
 */
@Path("/admin/deskriptoren")
public class AdminDeskriptorenResource {

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	@Inject
	DeskriptorenService deskriptorenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Deskriptor> loadDeskriptoren(@QueryParam(value = "kontext") final String kontext) {

		DeskriptorSuchkontext suchkontext = deskriptorenService.toDeskriptorSuchkontext(kontext);
		List<Deskriptor> trefferliste = deskriptorenRepository.listAll(Sort.by("name"));
		return deskriptorenService.filterByKontext(suchkontext, trefferliste);
	}

}
