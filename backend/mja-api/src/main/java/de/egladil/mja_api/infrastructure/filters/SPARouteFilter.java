// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

/**
 * SPARouteFilter
 */
public class SPARouteFilter {

	private static final Predicate<String> FILE_NAME_PREDICATE = Pattern.compile(".*[.][a-zA-Z\\d]+").asMatchPredicate();

	private static final String API_PREFIX = "/mja-api/";

	private static final String APP_PLUS_API_PREFIX = "/mja-app" + API_PREFIX;

	private static final String DEFAULT_APP = "/mja-app/";

	private static final Logger LOGGER = LoggerFactory.getLogger(SPARouteFilter.class);

	@RouteFilter(100)
	void apiFilter(final RoutingContext rc) {

		final String path = rc.normalizedPath();
		LOGGER.info("Check reroute with path: " + path);

		if (FILE_NAME_PREDICATE.test(path)) {

			LOGGER.info("keine Umleitung bei statischen Dateien");

			rc.next();
		} else if (path.startsWith(APP_PLUS_API_PREFIX)) {

			String rerouted = path.replaceFirst(DEFAULT_APP, "/");
			LOGGER.info("rc.reroute: " + rerouted);
			rc.reroute(rerouted);
		} else {

			LOGGER.info("global else => rc.next()");
			rc.next();
		}
	}
}
