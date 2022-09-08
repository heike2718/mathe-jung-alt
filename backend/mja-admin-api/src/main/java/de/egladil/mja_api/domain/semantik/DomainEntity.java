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
 * DomainEntity markiert eine DDD-Entity, d.h. ein Objekt mit einem Identifier, welches für sich persistiert werden könnte.
 */
public @interface DomainEntity {

}
