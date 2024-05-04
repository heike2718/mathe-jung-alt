// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.dto;

/**
 * Rectangle
 */
public class Rectangle {

	private final int width;

	private final int height;

	public Rectangle(final int width, final int height) {

		super();
		this.width = width;
		this.height = height;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}
}
