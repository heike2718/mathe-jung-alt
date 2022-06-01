// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.open;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorSuchkontext;
import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorenService;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.utils.HibernateParameterMapBuilder;
import io.quarkus.panache.common.Sort;

/**
 * AdminDeskriptorenResource
 */
@Path("/deskriptoren/v1")
public class OpenDeskriptorenResource {

	@Inject
	DeskriptorenService deskriptorenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Deskriptor> loadDeskriptoren(@QueryParam(value = "kontext") final DeskriptorSuchkontext suchkontext) {

		Map<String, Object> params = HibernateParameterMapBuilder.builder().put("admin", Boolean.FALSE).build();

		List<Deskriptor> trefferliste = Deskriptor.list("select d from Deskriptor d where d.admin = :admin",
			Sort.ascending("name"),
			params);

		return deskriptorenService.filterByKontext(suchkontext, trefferliste);
	}
}
