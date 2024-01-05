// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselMediensucheItemReadonly;

/**
 * QuelleInfosAdapter
 */
public class QuelleInfosAdapter {

	public IQuellenangabeDaten adapt(final PersistenteQuelleReadonly quelle) {

		return new IQuellenangabeDaten() {

			@Override
			public String getStufe() {

				return quelle.stufe;
			}

			@Override
			public String getSeite() {

				return quelle.seite;
			}

			@Override
			public Quellenart getQuellenart() {

				return quelle.quellenart;
			}

			@Override
			public String getPerson() {

				return quelle.person;
			}

			@Override
			public String getMediumTitel() {

				return quelle.mediumTitel;
			}

			@Override
			public String getKlasse() {

				return quelle.klasse;
			}

			@Override
			public String getJahr() {

				return quelle.jahr;
			}

			@Override
			public String getAutor() {

				return quelle.autor;
			}

			@Override
			public String getAusgabe() {

				return quelle.ausgabe;
			}
		};
	}

	public IQuellenangabeDaten adapt(final PersistentesRaetselMediensucheItemReadonly quelle) {

		return new IQuellenangabeDaten() {

			@Override
			public String getStufe() {

				return quelle.stufe;
			}

			@Override
			public String getSeite() {

				return quelle.seite;
			}

			@Override
			public Quellenart getQuellenart() {

				switch (quelle.medienart) {

				case BUCH:
					return Quellenart.BUCH;

				case INTERNET:
					return Quellenart.INTERNET;

				case ZEITSCHRIFT:
					return Quellenart.ZEITSCHRIFT;

				default:
					throw new IllegalArgumentException("Unexpected medienart: " + quelle.medienart);
				}
			}

			@Override
			public String getPerson() {

				return "";
			}

			@Override
			public String getMediumTitel() {

				return quelle.mediumTitel;
			}

			@Override
			public String getKlasse() {

				return quelle.klasse;
			}

			@Override
			public String getJahr() {

				return quelle.jahr;
			}

			@Override
			public String getAutor() {

				return quelle.autor;
			}

			@Override
			public String getAusgabe() {

				return quelle.ausgabe;
			}
		};

	}

}
