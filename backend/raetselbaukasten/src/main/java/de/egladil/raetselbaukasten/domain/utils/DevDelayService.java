// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.utils;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

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
