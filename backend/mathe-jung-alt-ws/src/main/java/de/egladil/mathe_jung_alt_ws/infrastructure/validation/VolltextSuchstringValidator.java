// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.infrastructure.validation;

import de.egladil.mathe_jung_alt_ws.infrastructure.validation.annotations.VolltextSuchstring;

/**
 * VolltextSuchstringValidator
 */
public class VolltextSuchstringValidator extends AbstractWhitelistValidator<VolltextSuchstring, String> {

	@Override
	protected String getWhitelist() {

		return "^[\\w äöüß (){}:%/=?'*~\"|&$\\^!;\\+\\-\\.\\,\\\\]{1,200}$";
	}

}
