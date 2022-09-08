// =====================================================
// Project: mja-shared
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.semantik;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

@Documented
@Target({ TYPE })
/**
 * ValueObject markiert ein DDD-ValueObject
 */
public @interface ValueObject {

}
