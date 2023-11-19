// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.dto;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import de.egladil.mja_api.domain.Suchmodus;

/**
 * Suchfilter
 */
@Schema(name = "Suchfilter", description = "ein Objekt, das Suchparameter enthält")
public class Suchfilter {

	@Schema(
		description = "ein String, mit dem gesucht wird. Worttrenner ist Leerzeichen. Es wird unter Berücksichtigung des Suchmodus gesucht.")
	private final String suchstring;

	@Schema(description = "ein kommaseparierter String, der IDs von Deskriptoren, mit denen gesucht wird")
	private final String deskriptorenIds;

	@Schema(description = "Modus, wie gesucht wird. INTERSECTION = AND, UNION = OR. Default ist UNION")
	private Suchmodus modus = Suchmodus.UNION;

	/**
	 * @param suchstring
	 * @param deskriptorenIds
	 */
	public Suchfilter(final String suchstring, final String deskriptorenIds) {

		this.suchstring = suchstring;
		this.deskriptorenIds = deskriptorenIds;
	}

	public String getSuchstring() {

		return suchstring;
	}

	public String getDeskriptorenIds() {

		return deskriptorenIds;
	}

	public SuchfilterVariante suchfilterVariante() {

		if (StringUtils.isAllBlank(suchstring, deskriptorenIds)) {

			throw new IllegalArgumentException("suchstring oder deskriptorenIds erforderlich");
		}

		if (StringUtils.isNoneBlank(suchstring, deskriptorenIds)) {

			return SuchfilterVariante.COMPLETE;
		}

		if (StringUtils.isNotBlank(suchstring)) {

			return SuchfilterVariante.VOLLTEXT;
		}

		return SuchfilterVariante.DESKRIPTOREN;

	}

	public Suchmodus getModus() {

		return modus;
	}

	public void setModus(final Suchmodus modus) {

		this.modus = modus;
	}

}
