// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;
import io.quarkus.panache.common.Sort;

/**
 * AdminDeskriptorenResource
 */
@Path("/admin/deskriptoren")
public class AdminDeskriptorenResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Deskriptor> loadDeskriptoren() {

		return Deskriptor.listAll(Sort.by("name"));
	}

}
