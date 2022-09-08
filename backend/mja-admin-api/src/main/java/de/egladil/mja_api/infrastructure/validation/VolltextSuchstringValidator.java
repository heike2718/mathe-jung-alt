// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.validation;

import de.egladil.mja_api.domain.annotations.VolltextSuchstring;

/**
 * VolltextSuchstringValidator
 */
public class VolltextSuchstringValidator extends AbstractWhitelistValidator<VolltextSuchstring, String> {

	@Override
	protected String getWhitelist() {

		return "^[\\w äöüß (){}:%/=?'*~\"|&$\\^!;\\+\\-\\.\\,\\\\]{1,200}$";
	}

}
