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

import de.egladil.mathe_jung_alt_ws.domain.dto.Suchfilter;
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
	public List<QuelleReadonly> findeQuellen(@QueryParam(value = "suchstring") @Pattern(
		regexp = "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$",
		message = "ungültige Eingabe: mindestens 1 höchstens 30 Zeichen, erlaubte Zeichen sind die deutschen Buchstaben, Ziffern, Leerzeichen und die Sonderzeichen +-_.,") final String suchstring, @QueryParam(
			value = "deskriptoren") @Pattern(
				regexp = "^[\\d\\,]{0,200}$",
				message = "ungültige Eingabe: höchstens 200 Zeichen, erlaubte Zeichen sind Zahlen und Komma") final String deskriptoren) {

		return quellenService.sucheQuellen(new Suchfilter(suchstring, deskriptoren));
	}
}