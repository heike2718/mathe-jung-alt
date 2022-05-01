// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.ws.admin;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.egladil.mathe_jung_alt_ws.domain.quellen.QuelleReadonly;
import de.egladil.mathe_jung_alt_ws.domain.quellen.QuellenService;

/**
 * AdminQuellenResource
 */
@Path("/admin/quellen")
public class AdminQuellenResource {

	@Inject
	QuellenService quellenService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<QuelleReadonly> findeQuellenMitText(@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$",
		message = "mindestens 1, höchsten 30 deutsche Buchstaben, Ziffern, Leerzeichen, +-_.,") final String suchstring) {

		System.out.println("suchstring = " + suchstring);

		return quellenService.sucheQuellen(suchstring);
	}
}
