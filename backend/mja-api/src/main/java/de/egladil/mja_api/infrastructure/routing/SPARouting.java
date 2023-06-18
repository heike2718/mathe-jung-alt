// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.routing;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;

/**
 * SPARouting
 */
@ApplicationScoped
public class SPARouting {

	private static final String API_PREFIX = "/mja-api/";

	private static final String APP_PLUS_API_PREFIX = "/mja-admin" + API_PREFIX;

	private static final String DEFAULT_APP = "/mja-admin/";

	private static final Logger LOGGER = LoggerFactory.getLogger(SPARouting.class);

	public void init(@Observes final Router router) {

		router.get("/*").handler(rc -> {

			final String path = rc.normalizedPath();

			LOGGER.debug("Check reroute with path {}", path);

			if (path.startsWith(APP_PLUS_API_PREFIX)) {

				String rerouted = path.replaceFirst(DEFAULT_APP, "/");
				LOGGER.info("rc.reroute: {}", rerouted);
				rc.reroute(rerouted);
			} else {

				LOGGER.debug("global else => rc.next()");
				rc.next();
			}

		});
	}

}
