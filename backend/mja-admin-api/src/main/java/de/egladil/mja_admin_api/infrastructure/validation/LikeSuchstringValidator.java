// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.infrastructure.validation;

import de.egladil.mja_admin_api.domain.validation.annotations.LikeSuchstring;

/**
 * LikeSuchstringValidator
 */
public class LikeSuchstringValidator extends AbstractWhitelistValidator<LikeSuchstring, String> {

	@Override
	protected String getWhitelist() {

		return "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$";
	}

}
