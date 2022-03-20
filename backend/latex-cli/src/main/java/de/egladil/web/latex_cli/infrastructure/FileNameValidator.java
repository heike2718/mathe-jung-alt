// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli.infrastructure;

import java.util.Arrays;
import java.util.List;

import de.egladil.web.latex_cli.exceptions.InvalidInputException;

/**
 * FileNameValidator
 */
public class FileNameValidator {

	private static final List<String> REJECTED_STRINGS = Arrays.asList(new String[] { "/", "\\", "%", ".", "%25", "%2e", "%2f" });

	public void validateFileName(final String queryParameterName, final String value) {

		for (String rejectable : REJECTED_STRINGS) {

			if (value.contains(rejectable)) {

				List<String> invalif = Arrays.asList(new String[] { queryParameterName + "=" + value });
				throw new InvalidInputException(invalif, "possible directory traversal attempt");
			}
		}

	}

}
