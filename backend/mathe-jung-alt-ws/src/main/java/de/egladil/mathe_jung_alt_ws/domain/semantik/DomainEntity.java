// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.semantik;

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
