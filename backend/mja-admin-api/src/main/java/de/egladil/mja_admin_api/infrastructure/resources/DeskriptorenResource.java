// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.egladil.mja_admin_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_admin_api.domain.deskriptoren.impl.DeskriptorenRepository;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.web.mja_shared.domain.deskriptoren.DeskriptorSuchkontext;
import io.quarkus.panache.common.Sort;

/**
 * DeskriptorenResource
 */
@Path("/deskriptoren/v1")
public class DeskriptorenResource {

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	@Inject
	DeskriptorenService deskriptorenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public List<Deskriptor> loadDeskriptoren(@QueryParam(value = "kontext") final DeskriptorSuchkontext suchkontext) {

		List<Deskriptor> trefferliste = deskriptorenRepository.listAll(Sort.ascending("name"));
		return deskriptorenService.filterByKontext(suchkontext, trefferliste);
	}

	@GET
	@Path("ids")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed("ADMIN")
	public String transformDeskriptorenStringToOrdinal(@QueryParam(value = "deskriptoren") @Pattern(
		regexp = "^[a-zA-ZäöüßÄÖÜ\\d\\,\\- ]{0,200}$",
		message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen, deutsche Buchstaben, Leerzeichen, Komma und Minus") final String deskriptoren) {

		return deskriptorenService.transformToDeskriptorenOrdinal(deskriptoren);
	}

}
