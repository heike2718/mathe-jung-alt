// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.sammlungen.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_admin_api.domain.sammlungen.dto.EditRaetselsammlungPayload;

/**
 * RaetselsammlungServiceImpl
 */
@ApplicationScoped
public class RaetselsammlungServiceImpl implements RaetselsammlungService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselsammlungServiceImpl.class);

	/**
	 * Erstellt eine neue Rätselsammlung. Dabei wird geprüft, ob es eine mit den Keys bereits gibt. In diesem Fall wird eine
	 * WebApplicationException mit Status 409 - Conflict geworfen.
	 *
	 * @param  payload
	 * @param  user
	 * @return         RaetselSucheTrefferItem
	 */
	@Override
	@Transactional
	public RaetselsucheTrefferItem raetselsammlungAnlegen(final EditRaetselsammlungPayload payload, final String user) {

		RaetselsucheTrefferItem result = new RaetselsucheTrefferItem();

		LOGGER.info("Rätselsammlung angelegt: {}, user={}", result.getId(), StringUtils.abbreviate(user, 11));

		return null;
	}
}
