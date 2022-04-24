// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.persistence.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * HibernateParameterMapBuilder ist ein fluent builder zum Erzeugen einer JPA-Parameter-Map
 */
public class HibernateParameterMapBuilder {

	private Map<String, Object> map;

	private HibernateParameterMapBuilder() {

		map = new HashMap<>();
	}

	public static HibernateParameterMapBuilder builder() {

		return new HibernateParameterMapBuilder();
	}

	public HibernateParameterMapBuilder put(final String key, final Object value) {

		map.put(key, value);
		return this;
	}

	public Map<String, Object> build() {

		return map;
	}

}
