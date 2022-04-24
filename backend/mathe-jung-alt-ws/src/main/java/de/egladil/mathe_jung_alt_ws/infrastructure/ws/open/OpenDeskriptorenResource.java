// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.open;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.utils.HibernateParameterMapBuilder;
import io.quarkus.panache.common.Sort;

/**
 * AdminDeskriptorenResource
 */
@Path("/deskriptoren")
public class OpenDeskriptorenResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Deskriptor> loadDeskriptoren() {

		Map<String, Object> params = HibernateParameterMapBuilder.builder().put("admin", Boolean.FALSE).build();

		return Deskriptor.list("select d from Deskriptor d where d.admin = :admin", Sort.by("name"),
			params);
	}

}
