//=====================================================
// Projekt: mathe-jung-alt-ws
// (c) HZD
//=====================================================

package de.egladil.mathe_jung_alt_ws.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;

/**
 * ApplicationLifeCycle
 */
@ApplicationScoped
public class ApplicationLifeCycle {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLifeCycle.class);

	// @formatter:off
	void onStart(@Observes StartupEvent event) {
	// @formatter:on
		LOGGER.info("====> The application is starting with profile " + ProfileManager.getActiveProfile());
	}
}
