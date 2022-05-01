// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.mathe_jung_alt_ws.infrastructure.validation;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * AbstractWhitelistValidator validiert Strings gegen eine Whitelist. Ist der Wert null oder leer, wird er als valid
 * angesehen. Bei Pflichtattributen müssen also zusätzliche Annotationen angebracht werden.
 */
public abstract class AbstractWhitelistValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

	@Override
	public void initialize(final A constraintAnnotation) {

		// nix
	}

	@Override
	public boolean isValid(final T value, final ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}

		if (!(value instanceof String)) {

			return false;
		}
		String strValue = (String) value;

		if (StringUtils.isBlank(strValue)) {

			return true;
		}

		Matcher matcher = getPattern().matcher(strValue);
		boolean matches = matcher.matches();
		return matches;
	}

	public Pattern getPattern() {

		Pattern pattern = Pattern.compile(getWhitelist());
		return pattern;
	}

	/**
	 * Gibt die Whitelist als regular expression zurück.
	 *
	 * @return
	 */
	protected abstract String getWhitelist();
}
