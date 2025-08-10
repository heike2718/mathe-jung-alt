// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistentesRaetselMediensucheItemReadonly;

/**
 * QuelleInfosAdapter
 */
public class QuelleInfosAdapter {

    /**
     * Adaptiert eine PersistenteQuelleReadonly
     * @param quelle PersistenteQuelleReadonly
     * @return IQuellenangabeDaten
     */
    public IQuellenangabeDaten adapt(final PersistenteQuelleReadonly quelle) {

        return new IQuellenangabeDaten() {

            @Override
            public String getStufe() {

                return quelle.getStufe();
            }

            @Override
            public String getSeite() {

                return quelle.getSeite();
            }

            @Override
            public Quellenart getQuellenart() {

                return quelle.getQuellenart();
            }

            @Override
            public String getPerson() {

                return quelle.getPerson();
            }

            @Override
            public String getMediumTitel() {

                return quelle.getMediumTitel();
            }

            @Override
            public String getKlasse() {

                return quelle.getKlasse();
            }

            @Override
            public String getJahr() {

                return quelle.getJahr();
            }

            @Override
            public String getAutor() {

                return quelle.getAutor();
            }

            @Override
            public String getAusgabe() {

                return quelle.getAusgabe();
            }
        };
    }

    /**
     * Adaptiert ein PersistentesRaetselMediensucheItemReadonly
     * @param quelle PersistentesRaetselMediensucheItemReadonly
     * @return IQuellenangabeDaten
     */
    public IQuellenangabeDaten adapt(final PersistentesRaetselMediensucheItemReadonly quelle) {

        return new IQuellenangabeDaten() {

            @Override
            public String getStufe() {

                return quelle.getStufe();
            }

            @Override
            public String getSeite() {

                return quelle.getSeite();
            }

            @Override
            public Quellenart getQuellenart() {

                switch (quelle.getMedienart()) {

                    case BUCH:
                        return Quellenart.BUCH;

                    case INTERNET:
                        return Quellenart.INTERNET;

                    case ZEITSCHRIFT:
                        return Quellenart.ZEITSCHRIFT;

                    default:
                        throw new IllegalArgumentException("Unexpected medienart: " + quelle.getMediumTitel());
                }
            }

            @Override
            public String getPerson() {

                return "";
            }

            @Override
            public String getMediumTitel() {

                return quelle.getMediumTitel();
            }

            @Override
            public String getKlasse() {

                return quelle.getKlasse();
            }

            @Override
            public String getJahr() {

                return quelle.getJahr();
            }

            @Override
            public String getAutor() {

                return quelle.getAutor();
            }

            @Override
            public String getAusgabe() {

                return quelle.getAusgabe();
            }
        };

    }

    /**
     * Adaptiert eine PersistenteAufgabeReadonly
     * @param ausDB PersistenteAufgabeReadonly
     * @return IQuellenangabeDaten
     */
    public IQuellenangabeDaten adapt(final PersistenteAufgabeReadonly ausDB) {

        return new IQuellenangabeDaten() {

            @Override
            public String getStufe() {

                return ausDB.getStufe();
            }

            @Override
            public String getSeite() {

                return ausDB.getSeite();
            }

            @Override
            public Quellenart getQuellenart() {

                return ausDB.getQuellenart();
            }

            @Override
            public String getPerson() {

                return ausDB.getPerson();
            }

            @Override
            public String getMediumTitel() {

                return ausDB.getMediumTitel();
            }

            @Override
            public String getKlasse() {

                return ausDB.getKlasse();
            }

            @Override
            public String getJahr() {

                return ausDB.getJahr();
            }

            @Override
            public String getAutor() {

                return ausDB.getAutor();
            }

            @Override
            public String getAusgabe() {

                return ausDB.getAusgabe();
            }
        };
    }

}
