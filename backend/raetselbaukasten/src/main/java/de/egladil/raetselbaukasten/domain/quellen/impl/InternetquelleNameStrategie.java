// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen.impl;

import org.apache.commons.lang3.StringUtils;

import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.quellen.IQuellenangabeDaten;
import de.egladil.raetselbaukasten.domain.quellen.QuelleNameStrategie;
import de.egladil.raetselbaukasten.domain.quellen.Quellenart;

/**
 * InternetquelleNameStrategie
 */
public class InternetquelleNameStrategie implements QuelleNameStrategie {
    @Override
    public String getText(final IQuellenangabeDaten quelle) {

        if (quelle.getQuellenart() != Quellenart.INTERNET) {

            throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.INTERNET);
        }

        if (StringUtils.isBlank(quelle.getMediumTitel())) {

            throw new MjaRuntimeException("Bei Quellenart INTERNET darf mediumTitel nicht blank sein.");
        }

        StringBuilder sb = new StringBuilder(quelle.getMediumTitel());

        if (StringUtils.isNotBlank(quelle.getJahr())) {

            sb.append(" (");
            sb.append(quelle.getJahr());
            sb.append(")");
        }

        if (StringUtils.isNotBlank(quelle.getKlasse())) {

            sb.append(", ");
            sb.append(quelle.getKlasse());
        }

        if (StringUtils.isNotBlank(quelle.getStufe())) {

            sb.append(", ");
            sb.append(quelle.getStufe());
        }

        return sb.toString();
    }

}
