// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain;

/**
 * SuchmodusDeskriptoren
 */
public enum SuchmodusDeskriptoren {

	LIKE,
	NOT_LIKE;

	public static SuchmodusDeskriptoren getDefault() {

		return SuchmodusDeskriptoren.LIKE;
	}
}
