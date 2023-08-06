// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.egladil.mja_api.infrastructure.validation.LikeSuchstringValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * LikeSuchstring
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Constraint(validatedBy = { LikeSuchstringValidator.class })
public @interface LikeSuchstring {

	String message() default "Ungültiger Wert";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] palyload() default {};

}
