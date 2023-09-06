// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * SPARedirectFilter
 */
@ApplicationScoped
public class SPARedirectFilter {

	private static final Predicate<String> FILE_NAME_PREDICATE = Pattern.compile(".*[.][a-zA-Z\\d]+").asMatchPredicate();

	private static final String API_PREFIX = "/mja-api/";

	private static final String ANGULAR_APP_BASE_URI = "/mja-app/";

	private static final Logger LOGGER = LoggerFactory.getLogger(SPARedirectFilter.class);

	public void init(@Observes final Router router) {

		router.get("/*").handler(rc -> {

			final String path = rc.normalizedPath();
			LOGGER.info(path);

			if ("/".equals(path)) {

				LOGGER.info("keine Umleitung bei /");

				rc.next();
			} else if (ANGULAR_APP_BASE_URI.equals(path)) {

				LOGGER.info("keine Umleitung, wenn bereits Startseite der Angular-Anwendung");

				rc.next();
			} else if (FILE_NAME_PREDICATE.test(path)) {

				LOGGER.info("keine Umleitung bei statischen Dateien");

				rc.next();
			} else if (path.startsWith(API_PREFIX)) {

				LOGGER.info("keine Umleitung bei backend calls");

				rc.next();
			} else {

				LOGGER.info("reroute zu {}", ANGULAR_APP_BASE_URI);
				rc.reroute(ANGULAR_APP_BASE_URI);
			}
		});
	}
}
