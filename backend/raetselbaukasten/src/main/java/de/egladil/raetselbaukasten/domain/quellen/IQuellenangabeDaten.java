// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

/**
 * IQuellenangabeDaten sind die Daten
 */
public interface IQuellenangabeDaten {

	Quellenart getQuellenart();

	String getMediumTitel();

	String getKlasse();

	String getStufe();

	String getAutor();

	String getAusgabe();

	String getJahr();

	String getSeite();

	String getPerson();
}
