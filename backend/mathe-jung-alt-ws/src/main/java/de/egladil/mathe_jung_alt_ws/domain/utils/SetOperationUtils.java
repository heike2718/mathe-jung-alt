// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * SetOperationUtils
 */
public class SetOperationUtils {

	/**
	 * @param  theSets
	 *                 Pair das die Mengen repräsentiert, die verglichen werden sollen.
	 * @return         boolean
	 */
	public boolean isLeftSubsetOfRight(final Pair<String, String> theSets) {

		List<String> tokensLeft = Arrays.asList(StringUtils.split(theSets.getLeft(), ','));
		List<String> tokensRight = Arrays.asList(StringUtils.split(theSets.getRight(), ','));

		for (String elementLeft : tokensLeft) {

			if (!tokensRight.contains(elementLeft)) {

				return false;
			}
		}

		return true;
	}

}
