// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.dto;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.SuchmodusDeskriptoren;
import de.egladil.mja_api.domain.SuchmodusVolltext;

/**
 * Suchfilter
 */
public class Suchfilter {

	private final String suchstring;

	private final String deskriptorenIds;

	private SuchmodusVolltext modusVolltext = SuchmodusVolltext.UNION;

	private SuchmodusDeskriptoren modusDeskriptoren = SuchmodusDeskriptoren.LIKE;

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

	public SuchmodusVolltext getModusVolltext() {

		return modusVolltext;
	}

	public void setModusVolltext(final SuchmodusVolltext modus) {

		this.modusVolltext = modus;
	}

	public SuchmodusDeskriptoren getModusDeskriptoren() {

		return modusDeskriptoren;
	}

	public void setModusDeskriptoren(final SuchmodusDeskriptoren modusDeskriptoren) {

		this.modusDeskriptoren = modusDeskriptoren;
	}

}
