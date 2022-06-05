// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	/**
	 * Verpackt die Deskriptoren in %% und gibt sie als kommaseparierten String zurück - für eine "deskriptoren like"- Suche.
	 *
	 * @param  deskriptorenIDs
	 *                         String kommaseparierte IDs von Deskriptoren.
	 * @return                 String
	 */
	public String prepareForDeskriptorenLikeSearch(final String deskriptorenIDs) {

		if (StringUtils.isBlank(deskriptorenIDs)) {

			return null;
		}

		String[] ids = StringUtils.split(deskriptorenIDs, ',');
		Set<Long> idsAsSet = Arrays.stream(ids).map(id -> Long.valueOf(id)).collect(Collectors.toSet());
		List<Long> idsAsList = new ArrayList<>(idsAsSet);
		Collections.sort(idsAsList);

		List<String> wrappedIds = idsAsList.stream().map(id -> "," + id + ",").collect(Collectors.toList());

		String result = StringUtils.join(wrappedIds, '%');
		return "%" + result + "%";

	}

}
