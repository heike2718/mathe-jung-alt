// =====================================================
// Project: mja-shared
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.semantik;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

@Documented
@Target({ TYPE })
/**
 * AggregateRoot markiert die Wurzel eines DDD-Aggregats.
 */
public @interface AggregateRoot {

}
