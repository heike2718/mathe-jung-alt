// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.validation;

import de.egladil.mja_api.domain.annotations.LikeSuchstring;

/**
 * LikeSuchstringValidator
 */
public class LikeSuchstringValidator extends AbstractWhitelistValidator<LikeSuchstring, String> {

	@Override
	protected String getWhitelist() {

		return "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$";
	}

}
