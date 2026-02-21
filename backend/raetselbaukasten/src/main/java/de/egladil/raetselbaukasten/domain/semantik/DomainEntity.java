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
 * DomainEntity markiert eine DDD-Entity, d.h. ein Objekt mit einem Identifier,
 * welches für sich persistiert werden könnte.
 */
public @interface DomainEntity {

}
