// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.validation;

import de.egladil.mathe_jung_alt_ws.infrastructure.validation.annotations.LikeSuchstring;

/**
 * LikeSuchstringValidator
 */
public class LikeSuchstringValidator extends AbstractWhitelistValidator<LikeSuchstring, String> {

	@Override
	protected String getWhitelist() {

		return "^[\\w äöüß \\+ \\- \\. \\,]{1,30}$";
	}

}
