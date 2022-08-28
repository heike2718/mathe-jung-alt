// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.utils;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 */
@ApplicationScoped
public class DevDelayService {

	@ConfigProperty(name = "delay.milliseconds", defaultValue = "0")
	long delayMillis = 0;

	public void pause() {

		if (delayMillis == 0) {

			return;
		}

		try {

			Thread.sleep(delayMillis);
		} catch (InterruptedException e) {

			//
		}
	}

}
