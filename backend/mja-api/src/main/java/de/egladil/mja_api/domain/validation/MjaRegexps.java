// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.validation;

/**
 * MjaRegexps
 */
public interface MjaRegexps {

	/**
	 * <ul>
	 * <li>/resources/4/4d367e6c-76e9.eps</li>
	 * <li>/resources/4/4d367e6c-02345.eps</li>
	 * <li>/resources/003/02324.eps</li>
	 * <li>/resources/003/02324_5.eps</li>
	 * </ul>
	 */
	String REGEXP_RELATIVE_PATH_EPS_IN_TEXT = "/resources/(\\d{3}|[a-f\\d]{1})/([\\d]{5}[_\\d]{0,2}|[a-f\\d]{8}\\-[a-f\\d]{4,5}[_\\d]{0,2})\\.eps";

	String VAILD_RELATIVE_PATH_EPS = "^" + REGEXP_RELATIVE_PATH_EPS_IN_TEXT + "$";

	/**
	 * Wenn ein File mit einem Namen wie dieses Pattern hochgeladen wird, soll dies als zweiter Teil des generierten Dateinamen
	 * genommen werden.
	 * <ul>
	 * <li>/02324.eps</li>
	 * <li>/02324_5.eps</li>
	 * </ul>
	 */
	String REGEXP_EPS_HEIKE_WITH_FILE_SEPERATOR = "^/([\\d]{5}_[\\d]{0,1}|[\\d]{5})\\.eps$";

	String VALID_EDIT_PAYLOOAD_SCHLUESSEL = "[\\d]*";

	String VALID_SCHLUESSEL = "^[\\d]{5}$";

	String VALID_DOMAIN_OBJECT_ID = "^[a-f\\d]{4}(?:[a-f\\d]{4}-){4}[a-f\\d]{12}|neu$";

	String VALID_SUCHSTRING = "^[^'\"<>]*$";

	String VALID_DATEINAME = "^[\\da-zA-ZÄÖÜäöüß_\\-\\.]{1,100}$";

	String VALID_JAHR = "^[\\d]{4}$";

	String VALID_AUSGABE = "^[\\d]{0,5}$";

	String VALID_KLASSE_STUFE = "^[\\w ]{0,10}$";

	String VALID_SEITE = "^[\\w -]{0,4}$";

	String VALID_PERSON = "^[^\"<>/#~%$]*$";

	String VALID_PFAD = "^/[\\w/.]*$";

	String VALID_URL = "^((https?|file)://(?:/)?[^\\s/$.?#].[^\\s]*|.{0})*$";

	String VALID_TITEL = "^[^\\^_\"%&$#{}~\\\\]*$";

	String VALID_ANTWORTVORSCHLAGTEXT = "^[^\\^_\"%&$#{}~\\\\]*$";

	String VALID_KOMMENTAR = "^[^'\"<>]*$";

}
