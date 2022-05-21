// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * UuidGenerator
 */
public class UuidGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(final SharedSessionContractImplementor session, final Object object) throws HibernateException {

		if (object instanceof PersistenteMjaEntity) {

			PersistenteMjaEntity entity = (PersistenteMjaEntity) object;

			if (entity.getImportierteUuid() != null) {

				return entity.getImportierteUuid();
			}
		}
		return UUID.randomUUID().toString();
	}

}
