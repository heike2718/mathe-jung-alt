// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import de.egladil.mja_api.domain.raetsel.Antwortvorschlag;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * AntwortvorschlaegeValidator
 */
public class AntwortvorschlaegeValidator implements ConstraintValidator<ValidAntwortvorschlaege, Antwortvorschlag[]> {

	private Validator validator;

	@Override
	public void initialize(final ValidAntwortvorschlaege constraintAnnotation) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public boolean isValid(final Antwortvorschlag[] value, final ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}

		List<String> violationMessages = new ArrayList<>();

		long anzahlKorrekt = Arrays.stream(value).filter(a -> a.isKorrekt()).count();

		if (anzahlKorrekt != 1) {

			String message = "anzahl korrekter Antwortvorschlaege muss 1 sein, war aber " + anzahlKorrekt;
			violationMessages.add(message);
		}

		for (int i = 0; i < value.length; i++) {

			Antwortvorschlag antwortvorschlag = value[i];
			Set<ConstraintViolation<Antwortvorschlag>> violations = validator.validate(antwortvorschlag);

			for (ConstraintViolation<Antwortvorschlag> violation : violations) {

				String message = String.format("Antwortvorschlag %d: %s", i, violation.getMessage());
				violationMessages.add(message);
			}
		}

		if (!violationMessages.isEmpty()) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(String.join(", ", violationMessages))
				.addConstraintViolation();
			return false;
		}

		return true;
	}

}
