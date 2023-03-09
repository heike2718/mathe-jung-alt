// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * HelloResource
 */
@Path("/hello")
@Tag(name = "Hello", description = "Funktionstest")
public class HelloResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {

		return "Ja, guten Tag auch, hier ist die mja-api";
	}

}
