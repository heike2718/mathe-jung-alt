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
 * PersonquelleNameStrategie
 */
public class PersonquelleNameStrategie implements QuelleNameStrategie {

    @Override
    public String getText(final IQuellenangabeDaten quelle) {

        if (quelle.getQuellenart() != Quellenart.PERSON) {

            throw new IllegalStateException("Funktioniert nur für Quellenart " + Quellenart.PERSON);
        }

        if (StringUtils.isBlank(quelle.getPerson())) {

            throw new MjaRuntimeException("Bei Quellenart PERSON darf person nicht blank sein.");
        }

        return quelle.getPerson();
    }

}
