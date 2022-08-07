// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.generatoren;

/**
 * ImageGeneratorService
 */
public interface ImageGeneratorService {

	/**
	 * Erzeugt eine LaTeX-Datei mit dem Image, compiliert sie zu einem png und gibt das png als byte-Array zurück.
	 *
	 * @param  relativerPfad
	 *                       String
	 * @return               byte[] ein png
	 */
	byte[] generiereGrafikvorschau(final String relativerPfad);

}
