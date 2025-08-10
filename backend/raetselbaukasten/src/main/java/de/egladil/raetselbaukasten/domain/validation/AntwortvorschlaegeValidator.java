// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.validation;

import de.egladil.raetselbaukasten.domain.raetsel.Antwortvorschlag;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 * AntwortvorschlaegeValidator
 */
//@Dependent
public class AntwortvorschlaegeValidator implements ConstraintValidator<ValidAntwortvorschlaege, Antwortvorschlag[]> {

    @Override
    public boolean isValid(final Antwortvorschlag[] value, final ConstraintValidatorContext context) {

        if (value == null || value.length == 0) {

            return true;
        }

        long anzahlKorrekt = Arrays.stream(value).filter(a -> a.isKorrekt()).count();

        if (anzahlKorrekt != 1) {

            String message = "anzahl korrekter Antwortvorschlaege muss 1 sein, war aber " + anzahlKorrekt;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }

        return true;
    }
}
