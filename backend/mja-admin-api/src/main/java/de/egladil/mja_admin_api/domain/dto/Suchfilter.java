// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * Suchfilter
 */
public class Suchfilter {

	private final String suchstring;

	private final String deskriptorenIds;

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

}
