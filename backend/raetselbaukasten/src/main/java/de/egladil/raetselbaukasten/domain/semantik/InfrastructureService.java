// =====================================================
// Project: mja-shared
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.semantik;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Documented
@Retention(CLASS)
@Target(TYPE)
/**
 * InfrastructureService markiert eine Klasse als DDD-InfrastructureService.
 */
public @interface InfrastructureService {

}
