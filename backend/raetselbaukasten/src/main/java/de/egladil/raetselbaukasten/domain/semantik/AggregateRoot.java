// =====================================================
// Project: mja-shared
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.semantik;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Target({ TYPE })
/**
 * AggregateRoot markiert die Wurzel eines DDD-Aggregats.
 */
public @interface AggregateRoot {

}
