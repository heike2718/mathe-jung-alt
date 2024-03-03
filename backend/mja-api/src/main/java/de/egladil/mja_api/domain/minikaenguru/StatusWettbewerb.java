// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.minikaenguru;

/**
 * StatusWettbewerb
 */
public enum StatusWettbewerb {

	ERFASST {

		@Override
		public boolean isAllowedForRestrictedAccessToWettbewerb() {

			return false;
		}
	},
	ANMELDUNG,
	DOWNLOAD_LEHRER,
	DOWNLOAD_PRIVAT,
	BEENDET;

	/**
	 * Um auch nicht freigegebene Minikänguru-Wettbewerbe für spezielle Zwecke herausgeben zu können, darf der Wettbewerb nicht in
	 * einem zu frühen Status sein, damit dessen Aufgaben nicht vorzeitig geleakt werden können.
	 *
	 * @return
	 */
	public boolean isAllowedForRestrictedAccessToWettbewerb() {

		return true;
	}
}
