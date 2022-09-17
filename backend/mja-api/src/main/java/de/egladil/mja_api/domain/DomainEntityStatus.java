// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DomainEntityStatus
 */
@Schema(name = "DomainEntityStatus", description = "Veröffentlichungsstatus eines Domain-Objekts")
public enum DomainEntityStatus {

	ERFASST,
	FREIGEGEBEN;

}
