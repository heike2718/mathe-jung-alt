// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.deskriptoren;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DeskriptorUI
 */
@Schema(description = "DTO für die Funktion eines Deskriptors bei der Suche.")
public class DeskriptorUI {

	@Schema(description = "technische ID")
	private Long id;

	@Schema(description = "Name zum Anzeigen")
	private String name;

	/**
	 *
	 */
	DeskriptorUI() {

		super();

	}

	/**
	 * @param id
	 * @param name
	 */
	public DeskriptorUI(final Long id, final String name) {

		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {

		return id;
	}

	public String getName() {

		return name;
	}

}
